package com.unagra.ebankingapi.service;

import com.unagra.ebankingapi.dto.OutgoingTransfersDTO;
import com.unagra.ebankingapi.dto.TransactionKeyDTO;
import com.unagra.ebankingapi.entities.ebanking.TransactionKey;
import com.unagra.ebankingapi.models.TransactionKeyResponse;
import com.unagra.ebankingapi.repository.ebanking.AccountsRepository;
import com.unagra.ebankingapi.repository.ebanking.TransactionKeyRepository;
import com.unagra.ebankingapi.utils.RandomValues.TrackingKeyRandom;
import com.unagra.ebankingapi.utils.RandomValues.TransactionKeyRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class TransactionKeyServiceImp implements TransactionKeyService {
    private String vlKindTransactionID = "";
    private String vlTransactionKeyValue = "";
    private String vlTrackingKeyValue = "";

    @Autowired
    private TransactionKeyRepository transactionKeyRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    @Override
    public TransactionKeyResponse newTransactionKey(Integer noChars, OutgoingTransfersDTO outgoingTransfersDTO,
                                                    String transactionDetailSealEncrypted) {
        // we need to generate the transactionKey according sourceid...
        if (outgoingTransfersDTO.getSourceid() == 1) {
            vlKindTransactionID = "UNG-E-";
        } else {
            vlKindTransactionID = "UNG-I-";
        }

        // transactionKey is only for 10 characters and it doesn't must exist in priors
        // records...
        // System.out.println("Creando transaction key");
        boolean vlTransactionKeyExist = true;
        while ( vlTransactionKeyExist == true ) {
            TransactionKeyRandom vlGetTransactionKeyValue = new TransactionKeyRandom();
            vlTransactionKeyValue = vlKindTransactionID + vlGetTransactionKeyValue.getTransactionKeyValue(4);
            System.out.println("TransactionKey generated: " + vlTransactionKeyValue);

            // lookup in previous records...
            vlTransactionKeyExist = transactionKeyRepository.findById(vlTransactionKeyValue).isPresent();
        }

        // trackingKey is only for 10 characters and it doesn't must exist in priors
        // records...
        // System.out.println("Creando tracking key");
        boolean vlTrackingKeyExist = false;
        while ( vlTrackingKeyExist == false ) {
            TrackingKeyRandom vlGetTrackingKeyValue = new TrackingKeyRandom();
            vlTrackingKeyValue = vlGetTrackingKeyValue.getTrackingKeyValue(2);
            System.out.println("TrackinigKey generated: " + vlTrackingKeyValue);

            // lookup in previous records...
            if (transactionKeyRepository.TrackingKeyExists(vlTrackingKeyValue) == null) {
                vlTrackingKeyExist = true;
            }
        }

        // get a catchmentfolio for this transaction...
        // System.out.println("Get catchmentfolio...");
        Long vlCatchmentFolio = accountsRepository.getCatchmentFolio(Long.parseLong("29"));
        // System.out.println(vlCatchmentFolio.toString());

        // get current dateTime action...
        // System.out.println("Save current transactionKey...");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DateFormat dateFormatYear = new SimpleDateFormat("yyyy");
        Date date = new Date();

        // now we need to save this records into the table...
        TransactionKeyDTO transactionKeyDTO = new TransactionKeyDTO()
                .builder()
                .trackingkey(vlTrackingKeyValue)
                .transactiondate(date)
                .catchmentfolio(vlCatchmentFolio)
                .transactiondetailseal(transactionDetailSealEncrypted)
                .transactionkeyId(vlTransactionKeyValue)
                .year(Integer.parseInt(dateFormatYear.format(date).toString()))
                .build();

        // convert DTO to Entity...
        TransactionKey transactionKey = mapToEntity(transactionKeyDTO);

        // Save Data...
        TransactionKey newRecord = transactionKeyRepository.save(transactionKey);

        // return values...
        TransactionKeyResponse transactionKeyResponse = new TransactionKeyResponse()
                .builder()
                .dateTimeResponse(dateFormat.format(date))
                .transactionKey(vlTrackingKeyValue)
                .msg("Se ha generado una nueva clave de identificación ["
                        + vlTransactionKeyValue
                        + "] para la transacción que esta a punto de realizar.")
                .build();
        return transactionKeyResponse;
    }

    // convert Entity to DTO...
    private TransactionKeyDTO mapToDTO(TransactionKey transactionKey) {
        TransactionKeyDTO transactionKeyDTO = new TransactionKeyDTO();
        transactionKeyDTO.setTrackingkey(transactionKey.getTrackingkey());
        transactionKeyDTO.setTransactiondate(transactionKey.getTransactiondate());
        transactionKeyDTO.setTransactiondatetime(transactionKey.getTransactiondatetime());
        transactionKeyDTO.setTransactiondetailseal(transactionKey.getTransactiondetailseal());
        transactionKeyDTO.setTransactionkeyId(transactionKey.getTransactionkeyid());
        transactionKeyDTO.setYear(transactionKey.getYear());
        return transactionKeyDTO;
    }

    // convert DTO to Entity...
    private TransactionKey mapToEntity(TransactionKeyDTO transactionKeyDTO) {
        TransactionKey transactionKey = new TransactionKey();
        transactionKey.setTrackingkey(transactionKeyDTO.getTrackingkey());
        transactionKey.setTransactiondate(transactionKeyDTO.getTransactiondate());
        transactionKey.setTransactiondatetime(transactionKeyDTO.getTransactiondatetime());
        transactionKey.setTransactiondetailseal(transactionKeyDTO.getTransactiondetailseal());
        transactionKey.setTransactionkeyid(transactionKeyDTO.getTransactionkeyId());
        transactionKey.setYear(transactionKeyDTO.getYear());
        return transactionKey;
    }
}
