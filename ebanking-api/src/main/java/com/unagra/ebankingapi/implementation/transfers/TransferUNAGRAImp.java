package com.unagra.ebankingapi.implementation.transfers;

import com.unagra.ebankingapi.dto.CatchmentMovementsDTO;
import com.unagra.ebankingapi.dto.OutgoingTransfersDTO;
import com.unagra.ebankingapi.entities.bancaUNAGRA.CatchmentMovements;
import com.unagra.ebankingapi.entities.ebanking.UpdateAccountBalanceATM;
import com.unagra.ebankingapi.models.TransactionKeyResponse;
import com.unagra.ebankingapi.repository.bancaUNAGRA.CatchmentMovementsRepository;
import com.unagra.ebankingapi.repository.ebanking.AccountExtraInfoRepository;
import com.unagra.ebankingapi.repository.ebanking.AccountsRepository;
import com.unagra.ebankingapi.repository.ebanking.UpdateAccountBalanceATMRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransferUNAGRAImp {
    // Setup values for transfers between UNAGRA accounts...
    private static final Integer vlMovementTransferUNAGRA_Charge = 43;
    private static final Integer vlMovementTransferUNAGRA_Payment = 44;
    private static final Integer vlOriginTransferUNAGRA = 3;
    private static final Integer vlUserProcess = 1120;

    //Recovery values from current transaction...
    private Long vlTransferID_Charge;
    private Long vlTransferID_Payment;


    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private AccountExtraInfoRepository accountExtraInfoRepository;

    @Autowired
    private CatchmentMovementsRepository catchmentMovementsRepository;

    @Autowired
    private UpdateAccountBalanceATMRepository updateAccountBalanceATMRepository;


    public List<String> processTransferUNAGRA(OutgoingTransfersDTO outgoingTransfersDTO,
                                              TransactionKeyResponse transactionKeyResponse) {

        // get current dateTime action...
        DateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DateFormat yearFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();
        List<String> vlResult = new ArrayList<>();

        // 1.- get extra info about customer and accountid (charage)...
        Double vlCurrentBalance_C = accountsRepository.getBalanceByAccountID(outgoingTransfersDTO.getAccountid_origin());
        Integer vlSubsidiaryID_C = accountExtraInfoRepository
                .findSubsidiaryByAccountID(outgoingTransfersDTO.getAccountid_origin());
        String vlMovementDescription = catchmentMovementsRepository
                .getMovementDescription(vlMovementTransferUNAGRA_Charge.toString())
                .trim()
                .toUpperCase();

        // 2.- adding values to DTO catchmentMovment from our outgoingTransfersDTO...
        CatchmentMovementsDTO vlCMDTO_C = new CatchmentMovementsDTO()
                .builder()
                .anio(Integer.parseInt(yearFormat.format(date)))
                .asentado(Integer.parseInt("1"))
                .authidresponse("")
                .clienteid(outgoingTransfersDTO.getCustomerid_origin())
                .cuentaid(outgoingTransfersDTO.getAccountid_origin())
                .denominacionid(Long.parseLong("0"))
                .descripcion(vlMovementDescription)
                .estatusid("T")
                .fechaaplicacion(date)
                .fechamvto(date)
                .foliooperacion(transactionKeyResponse.getCatchmentfolio())
                //vlCMDTO.setImpcomision(null);
                .intneto(Integer.parseInt("0"))
                .isr(Integer.parseInt("0"))
                //vlCMDTO.setMasteracctnum("");
                .monedaid(Integer.parseInt("1"))
                .monto(outgoingTransfersDTO.getAmount())
                .origenid(vlOriginTransferUNAGRA)
                //vlCMDTO.setPrimaryacctnum(null);
                .saldoinicial(vlCurrentBalance_C)
                .saldoinicialinversiones(Double.parseDouble("0"))
                .sucursaldestino(vlSubsidiaryID_C)
                .sucursalid(vlSubsidiaryID_C)
                .sujetoide(Integer.parseInt("0"))
                .ta(Integer.parseInt("0"))
                //vlCMDTO.setTerminalid(null);
                .tipomovimientoid(vlMovementTransferUNAGRA_Charge)
                .transactionid(Long.parseLong("0"))
                .usuarioid(vlUserProcess)
                .build();


        //3.- passing DTO to Entity to save data...
        CatchmentMovements vlEntity_C = mapToEntity(vlCMDTO_C);
        CatchmentMovements vlNewRecord_C = catchmentMovementsRepository.save(vlEntity_C);
        vlTransferID_Charge = vlNewRecord_C.getMovimientoid();

        //4.- update balance...
        catchmentMovementsRepository.updateBalanceBIU((vlCurrentBalance_C - vlCMDTO_C.getMonto()), vlCMDTO_C.getCuentaid());

        //5.- update balance to ATM...
        UpdateAccountBalanceATM updateAccountBalanceATM_C = new UpdateAccountBalanceATM();
        updateAccountBalanceATM_C.setAccountid(outgoingTransfersDTO.getAccountid_origin());
        updateAccountBalanceATM_C.setYear(Integer.parseInt(yearFormat.format(date)));
        updateAccountBalanceATM_C.setTransactiondate(date);
        updateAccountBalanceATM_C.setCustomerid(outgoingTransfersDTO.getCustomerid_origin());
        updateAccountBalanceATM_C.setProccessed(Integer.parseInt("0"));
        updateAccountBalanceATM_C.setTrasactiondatetime(date);
        updateAccountBalanceATMRepository.save(updateAccountBalanceATM_C);


        /**
         *Now we need to add second movement about this transaction,
         * this movement will be applied to beneficiary customer.
         */

        // 1.- get extra info about customer and accountid (payment)...
        Double vlCurrentBalance_P = accountsRepository.getBalanceByAccountID(outgoingTransfersDTO.getAccountid_destination());
        Integer vlSubsidiaryID_P = accountExtraInfoRepository
                .findSubsidiaryByAccountID(outgoingTransfersDTO.getAccountid_destination());
        vlMovementDescription = catchmentMovementsRepository
                .getMovementDescription(vlMovementTransferUNAGRA_Payment.toString())
                .trim()
                .toUpperCase();

        // 2.- adding values to DTO catchmentMovment from our outgoingTransfersDTO...
        CatchmentMovementsDTO vlCMDTO_P = new CatchmentMovementsDTO()
                .builder()
                .anio(Integer.parseInt(yearFormat.format(date)))
                .asentado(Integer.parseInt("1"))
                .authidresponse("")
                .clienteid(outgoingTransfersDTO.getCustomerid_destination())
                .cuentaid(outgoingTransfersDTO.getAccountid_destination())
                .denominacionid(Long.parseLong("0"))
                .descripcion(vlMovementDescription)
                .estatusid("T")
                .fechaaplicacion(date)
                .fechamvto(date)
                .foliooperacion(transactionKeyResponse.getCatchmentfolio())
                //vlCMDTO.setImpcomision(null);
                .intneto(Integer.parseInt("0"))
                .isr(Integer.parseInt("0"))
                //vlCMDTO.setMasteracctnum("");
                .monedaid(Integer.parseInt("1"))
                .monto(outgoingTransfersDTO.getAmount())
                .origenid(vlOriginTransferUNAGRA)
                //vlCMDTO.setPrimaryacctnum(null);
                .saldoinicial(vlCurrentBalance_P)
                .saldoinicialinversiones(Double.parseDouble("0"))
                .sucursaldestino(vlSubsidiaryID_P)
                .sucursalid(vlSubsidiaryID_C)
                .sujetoide(Integer.parseInt("0"))
                .ta(Integer.parseInt("0"))
                //vlCMDTO.setTerminalid(null);
                .tipomovimientoid(vlMovementTransferUNAGRA_Payment)
                .transactionid(Long.parseLong("0"))
                .usuarioid(vlUserProcess)
                .build();


        //3.- passing DTO to Entity to save data...
        CatchmentMovements vlEntity_P = mapToEntity(vlCMDTO_C);
        CatchmentMovements vlNewRecord_P = catchmentMovementsRepository.save(vlEntity_P);
        vlTransferID_Payment = vlNewRecord_P.getMovimientoid();

        //4.- update balance...
        catchmentMovementsRepository.updateBalanceBIU((vlCurrentBalance_P - vlCMDTO_P.getMonto()), vlCMDTO_P.getCuentaid());

        //5.- update balance to ATM...
        UpdateAccountBalanceATM updateAccountBalanceATM_P = new UpdateAccountBalanceATM();
        updateAccountBalanceATM_P.setAccountid(outgoingTransfersDTO.getAccountid_destination());
        updateAccountBalanceATM_P.setYear(Integer.parseInt(yearFormat.format(date)));
        updateAccountBalanceATM_P.setTransactiondate(date);
        updateAccountBalanceATM_P.setCustomerid(outgoingTransfersDTO.getCustomerid_destination());
        updateAccountBalanceATM_P.setProccessed(Integer.parseInt("0"));
        updateAccountBalanceATM_P.setTrasactiondatetime(date);
        updateAccountBalanceATMRepository.save(updateAccountBalanceATM_P);

        //must be registered into history of transactions of current day...
        

        //return values from this operation...
        vlResult.add(vlTransferID_Charge.toString());
        vlResult.add(vlTransferID_Payment.toString());
        return vlResult;
    }

    // convert DTO to Entity...
    private CatchmentMovements mapToEntity(CatchmentMovementsDTO catchmentMovementsDTO) {
        CatchmentMovements catchmentMovements = new CatchmentMovements();
        catchmentMovements.setAnio(catchmentMovementsDTO.getAnio());
        catchmentMovements.setAsentado(catchmentMovementsDTO.getAsentado());
        catchmentMovements.setAuthidresponse(catchmentMovementsDTO.getAuthidresponse());
        catchmentMovements.setClienteid(catchmentMovementsDTO.getClienteid());
        catchmentMovements.setCuentaid(catchmentMovementsDTO.getCuentaid());
        catchmentMovements.setDenominacionid(catchmentMovementsDTO.getDenominacionid());
        catchmentMovements.setDescripcion(catchmentMovementsDTO.getDescripcion());
        catchmentMovements.setEstatusid(catchmentMovementsDTO.getEstatusid());
        catchmentMovements.setFechaaplicacion(catchmentMovementsDTO.getFechaaplicacion());
        catchmentMovements.setFechamvto(catchmentMovementsDTO.getFechamvto());
        catchmentMovements.setFoliooperacion(catchmentMovementsDTO.getFoliooperacion());
        //vlCMDTO.setImpcomision(null);
        catchmentMovements.setIntneto(catchmentMovementsDTO.getIntneto());
        catchmentMovements.setInversionid(catchmentMovementsDTO.getInversionid());
        catchmentMovements.setIsr(catchmentMovementsDTO.getIsr());
        //vlCMDTO.setMasteracctnum("");
        catchmentMovements.setMonedaid(catchmentMovementsDTO.getMonedaid());
        catchmentMovements.setMonto(catchmentMovementsDTO.getMonto());
        catchmentMovements.setOrigenid(catchmentMovementsDTO.getOrigenid());
        //vlCMDTO.setPrimaryacctnum(null);
        catchmentMovements.setSaldoinicial(catchmentMovementsDTO.getSaldoinicial());
        catchmentMovements.setSaldoinicialinversiones(catchmentMovementsDTO.getSaldoinicialinversiones());
        catchmentMovements.setSucursaldestino(catchmentMovementsDTO.getSucursaldestino());
        catchmentMovements.setSucursalid(catchmentMovementsDTO.getSucursalid());
        catchmentMovements.setSujetoide(catchmentMovementsDTO.getSujetoide());
        catchmentMovements.setTa(catchmentMovementsDTO.getTa());
        //vlCMDTO.setTerminalid(null);
        catchmentMovements.setTipomovimientoid(catchmentMovementsDTO.getTipomovimientoid());
        catchmentMovements.setTransactionid(catchmentMovementsDTO.getTransactionid());
        catchmentMovements.setUsuarioid(catchmentMovementsDTO.getUsuarioid());
        return catchmentMovements;
    }
}
