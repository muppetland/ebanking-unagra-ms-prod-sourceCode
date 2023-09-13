package com.unagra.banking.institutions.api.services;

import com.unagra.banking.institutions.api.dto.BankingInstitutionsDTO;
import com.unagra.banking.institutions.api.entities.BankingInstitutionsCatalogue;
import com.unagra.banking.institutions.api.entities.DetailBankInstitution;
import com.unagra.banking.institutions.api.exceptions.ResourceNotFoundException;
import com.unagra.banking.institutions.api.model.BankingInstitutionsCatalogueResponse;
import com.unagra.banking.institutions.api.model.DetailbyBankingInstitutionResponse;
import com.unagra.banking.institutions.api.repository.BankingInstitutionsCatalogueRepository;
import com.unagra.banking.institutions.api.repository.DetailBankInstitutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BankingInstitutionsCatalogueServiceImp implements BankingInstitutionsCatalogueService {
    @Autowired
    private BankingInstitutionsCatalogueRepository bankingInstitutionsRepository;

    @Autowired
    private DetailBankInstitutionRepository detailBankInstitutionRepository;

    @Override
    public BankingInstitutionsCatalogueResponse getBankingInstitutiosAvailableArray() {
        // get all records from this view...
        List<BankingInstitutionsCatalogue> bankingInstitutionsArray = bankingInstitutionsRepository
                .findAll(Sort.by(Sort.Direction.DESC, "institution"));

        // get current dateTime action...
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        // return model with data...
        BankingInstitutionsCatalogueResponse bankingInstitutionsResponse = new BankingInstitutionsCatalogueResponse()
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

    @Override
    public DetailbyBankingInstitutionResponse getDetailByBankInstitution(String keymatch) {
        //find key value sent...
        DetailBankInstitution detailBankInstitution = detailBankInstitutionRepository
                .findById(keymatch)
                .orElseThrow(() -> new ResourceNotFoundException(keymatch));

        //parse this current value to response...
        //List<DetailbyBankingInstitutionResponse> finalList = new ArrayList<>();
        DetailbyBankingInstitutionResponse detailbyBankingInstitutionResponse = new DetailbyBankingInstitutionResponse()
                .builder()
                .institution(detailBankInstitution.getInstitution().trim())
                .cvespei(detailBankInstitution.getCvespei())
                .iscard(detailBankInstitution.getIscard())
                .keymatch(detailBankInstitution.getKeymatch().trim())
                .build();
        //finalList.add(detailbyBankingInstitutionResponse);
        return detailbyBankingInstitutionResponse;
    }

    // convert Entity to DTO...
    private BankingInstitutionsDTO mapToDTO(BankingInstitutionsCatalogue bankingInstitutions) {
        BankingInstitutionsDTO bankingInstitutionsDTO = new BankingInstitutionsDTO();
        bankingInstitutionsDTO.setId(bankingInstitutions.getId());
        bankingInstitutionsDTO.setInstitution(bankingInstitutionsDTO.getInstitution());
        return bankingInstitutionsDTO;
    }
}
