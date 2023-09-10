package com.unagra.ebankingapi.service;

import com.unagra.ebankingapi.dto.BankingInstitutionsDTO;
import com.unagra.ebankingapi.entities.ebanking.BankingInstitutions;
import com.unagra.ebankingapi.models.BankingInstitutionsResponse;
import com.unagra.ebankingapi.repository.ebanking.BankingInstitutionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class BankingInstitutionsServiceImp implements BankingInstitutionsService {
    @Autowired
    private BankingInstitutionsRepository bankingInstitutionsRepository;

    @Override
    public BankingInstitutionsResponse getBankingInstitutiosAvailableArray() {
        // get all records from this view...
        List<BankingInstitutions> bankingInstitutionsArray = bankingInstitutionsRepository
                .findAll(Sort.by(Sort.Direction.DESC, "institution"));

        //if we don't have information...
        if (bankingInstitutionsArray == null) {
            throw new RuntimeException("¡Ups!, lo sentimos, al parecer no contamos con información de las instituciones bancarias, si no tenemos contamos con este catálogo no se porá realizar la operación SPEI solicitada.");
        }

        // get current dateTime action...
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        // return model with data...
        BankingInstitutionsResponse bankingInstitutionsResponse = new BankingInstitutionsResponse()
                .builder()
                .bankingInstitutionsDetail(bankingInstitutionsArray)
                .bankingInstitutionsToital(bankingInstitutionsArray.size())
                .dateTimeResponse(dateFormat.format(date))
                .msg("Se tienen " + bankingInstitutionsArray.size() + " instituciones bancarias disponibles.")
                .build();

        // ().add(bankingInstitutionsArray.stream().map(loginView ->
        // mapToDTO(loginView)).collect(Collectors.toList()));
        return bankingInstitutionsResponse;
    }

    // convert Entity to DTO...
    private BankingInstitutionsDTO mapToDTO(BankingInstitutions bankingInstitutions) {
        BankingInstitutionsDTO bankingInstitutionsDTO = new BankingInstitutionsDTO();
        bankingInstitutionsDTO.setId(bankingInstitutions.getId());
        bankingInstitutionsDTO.setInstitution(bankingInstitutionsDTO.getInstitution());
        return bankingInstitutionsDTO;
    }
}
