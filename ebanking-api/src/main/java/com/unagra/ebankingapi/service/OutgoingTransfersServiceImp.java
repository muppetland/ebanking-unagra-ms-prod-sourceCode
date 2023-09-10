package com.unagra.ebankingapi.service;

import com.unagra.ebankingapi.dto.CatchmentMovementsDTO;
import com.unagra.ebankingapi.dto.OutgoingTransfersDTO;
import com.unagra.ebankingapi.exceptions.ResourceNotFoundException;
import com.unagra.ebankingapi.implementation.transfers.TransferUNAGRAImp;
import com.unagra.ebankingapi.models.OutgoingTransfersResponse;
import com.unagra.ebankingapi.models.TransactionKeyResponse;
import com.unagra.ebankingapi.repository.ebanking.AccountsRepository;
import com.unagra.ebankingapi.repository.ebanking.OtpTokenRepository;
import com.unagra.ebankingapi.repository.ebanking.ThirdsAccountsRepository;
import com.unagra.ebankingapi.repository.ebanking.TransactionKeyRepository;
import com.unagra.ebankingapi.utils.AES.AlgorithAESCBC;
import jakarta.ws.rs.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutgoingTransfersServiceImp implements OutgoingTransfersService {
    @Value("${ebanking.vector}")
    public String vpuVector;
    @Value("${ebanking.password}")
    public String vpuKey;
    @Value("${ebanking.minimumAmount}")
    public Double vlMinimumAmount;
    @Value("${ebanking.lapsedMinutsBetweenTransfer}")
    public Integer vlLapsedMinutsBetweenTransfer;

    @Autowired
    private TransactionKeyService transactionKeyService;

    @Autowired
    private TransactionKeyRepository transactionKeyRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private ThirdsAccountsRepository thirdsAccountsRepository;

    @Autowired
    private CatchmentMovementsService catchmentMovementsService;

    @Autowired
    private OtpTokenRepository otpTokenRepository;

    @Override
    public OutgoingTransfersResponse newTransferUNAGRA(OutgoingTransfersDTO outgoingTransfersDTO) {
        // 1.- if token sent doesn't valid, we can't continues...
        String vlValidateToken = otpTokenRepository.findTokenByCustomer(outgoingTransfersDTO.getCustomerid_origin(), outgoingTransfersDTO.getToken());
        if (vlValidateToken == null) {
            throw new ResourceNotFoundException("El token proporcionado no tiene coincidencia con el proceso realizado por el clienteID [" + outgoingTransfersDTO.getCustomerid_origin().toString() + "], favor de proporcionar el token correcto para esta solicitud.");
        }

        // SourceID: 'cause transactions assigned to UNAGRA has this value...
        // before to start with the new transaction for UNAGRA's accounts we need to
        // validate some thing...

        // 2.- customerID sent must exists...
        accountsRepository.findById(outgoingTransfersDTO.getCustomerid_origin()).orElseThrow(() -> new ResourceNotFoundException("El clienteID [" + outgoingTransfersDTO.getCustomerid_origin() + "] que intenta realizar la transacción no se encuentra registrado."));

        // 3.- account sent must exists...
        Long vlAccountExists = accountsRepository.findAccountID(outgoingTransfersDTO.getAccountid_origin());
        if (vlAccountExists == null) {
            // notice...
            throw new ResourceNotFoundException("La cuentaID [" + outgoingTransfersDTO.getAccountid_origin() + "] no se encuentra registrada.");
        }

        // 4.- accountID sent must be assigned to current customerID...
        Long vlAccountAssigned = thirdsAccountsRepository.findAccountIDByCustomerID(outgoingTransfersDTO.getCustomerid_origin(), outgoingTransfersDTO.getAccountid_destination().toString(), 1);
        if (vlAccountAssigned == null) {
            // notice...
            throw new ResourceNotFoundException("La cuentaID [" + outgoingTransfersDTO.getAccountid_destination() + "] no esta relacionada al clienteID [" + outgoingTransfersDTO.getCustomerid_origin() + "], imposible continuar con la operación.");
        }

        // 5.- we need to know if this account has enough balance to proceed with this
        // transaction...
        Double vlCurrentBalance = accountsRepository.getBalanceByAccountID(outgoingTransfersDTO.getAccountid_origin());
        if ((vlCurrentBalance - vlMinimumAmount) < outgoingTransfersDTO.getAmount()) {
            // notice...
            throw new ResourceNotFoundException("La cuentaID [" + outgoingTransfersDTO.getAccountid_origin() + "] no cuenta con el saldo suficiente para realizar esta operación.");
        }

        // 6.- The amount for this transaction, must be less or equal than limit amount
        // setup...
        Double vlLimitAmount = thirdsAccountsRepository.validateLimitAmount(outgoingTransfersDTO.getCustomerid_origin(), outgoingTransfersDTO.getAccountid_destination().toString(), 1, outgoingTransfersDTO.getAmount());
        // System.out.println(outgoingTransfersDTO.getAmount());
        // System.out.println(vlLimitAmount);
        if (vlLimitAmount == null) {
            // notice...
            throw new ResourceNotFoundException("La transacción que desea realizar para la cuentaID [" + outgoingTransfersDTO.getAccountid_destination() + "] por el monto de [" + outgoingTransfersDTO.getAmount() + "] excede el monto limite previamente configurado, favor de revisar la configiración para esta cuenta y hacer el ajuste correspondiente.");
        }

        /*
         * 7.- we need to encrypt values from detail of transaction to setup into
         * "transactiondetailseal" field...
         *
         * this seal will build by following params...
         *
         * 1.- sourceid 2.- customerid_origin 3.- accountid_origin 4.-
         * customerid_destination 5.- accountid_destination 6.- destinationaccountcard
         * 7.- concept 8.- amount 9.- institutionid 10.- numericalreference 11.-
         * commissionid
         */

        AlgorithAESCBC algorithAESCBC = new AlgorithAESCBC();
        String vlTransactionDetailSeal = outgoingTransfersDTO.getSourceid().toString() + outgoingTransfersDTO.getCustomerid_origin().toString() + outgoingTransfersDTO.getAccountid_origin().toString() + outgoingTransfersDTO.getCustomerid_destination().toString() + outgoingTransfersDTO.getAccountid_destination().toString() + outgoingTransfersDTO.getDestinationaccountcard() + outgoingTransfersDTO.getConcept() + outgoingTransfersDTO.getAmount().toString() + outgoingTransfersDTO.getInstitutionid().toString() + outgoingTransfersDTO.getNumericalreference().toString() + outgoingTransfersDTO.getCommissionid().toString();
        String vlTransactionDetailSealEncrypted = algorithAESCBC.resultadoEncriptado(vlTransactionDetailSeal, vpuKey, vpuVector);

        // 8.- If we have a same transaction this data will be found in the field
        // "transactiondetailseal", this transaction must be at least 1 minute between
        // prior transfer and this one to be process...
        // System.out.println("Paso 7");
        Integer vlLaspsedMinutes = transactionKeyRepository.validateTransactionDetailSeal(vlTransactionDetailSealEncrypted);
        if (vlLaspsedMinutes != null) {
            if (vlLaspsedMinutes <= vlLapsedMinutsBetweenTransfer) {
                // notice...
                throw new BadRequestException("La transacción que desea realizar para la cuentaID [" + outgoingTransfersDTO.getAccountid_destination() + "] por el monto de [" + outgoingTransfersDTO.getAmount() + "], no puede ser procesada por temas de seguridad, debido a que tenemos una similitud en una transferencia realizada previamente hace menos de " + vlLapsedMinutsBetweenTransfer + " minuto(s) con los mismos datos.");
            }
        } else {
            System.out.println("No existe la transferencia...");
        }

        // 9.- we need to create the transactionKey before to continues with the process of new transaction...
        TransactionKeyResponse transactionKeyResponse = new TransactionKeyResponse();
        transactionKeyResponse = transactionKeyService.newTransactionKey(2, outgoingTransfersDTO, vlTransactionDetailSealEncrypted);

        // 10.- Inserting transactions for ordenant and beneficiary...
        // charge movement...
        TransferUNAGRAImp vlNewTransfer = new TransferUNAGRAImp();
        List<String> vlChargeMovementID = vlNewTransfer.processTransferUNAGRA(outgoingTransfersDTO, transactionKeyResponse);

        // 11.- If we have a same transaction this data will be found in the field
        // System.out.println("Paso 9");
        CatchmentMovementsDTO vlData = catchmentMovementsService.getDataFromMovementID(Long.parseLong("1"));
        // System.out.println("Descripcion Mov : " + vlData.getDescripcion());

        // System.out.println("Damos respuesta...");
        OutgoingTransfersResponse outgoingTransfersResponse = new OutgoingTransfersResponse();
        outgoingTransfersResponse.setTransactionkey(transactionKeyResponse.getTransactionKey());

        // close token sent to prevent use it in others services...
        otpTokenRepository.updateStatusTokenByCustomer(outgoingTransfersDTO.getCustomerid_origin(), outgoingTransfersDTO.getToken());

        return outgoingTransfersResponse;
    }

}
