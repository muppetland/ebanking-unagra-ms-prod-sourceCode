package com.unagra.ebankingapi.service;

import com.unagra.ebankingapi.entities.ebanking.AccountExtraInfo;
import com.unagra.ebankingapi.exceptions.ResourceNotFoundException;
import com.unagra.ebankingapi.models.AccountExtraInfoResponse;
import com.unagra.ebankingapi.repository.ebanking.AccountExtraInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class AccountExtraInfoServiceImp implements AccountExtraInfoService {
    @Autowired
    private AccountExtraInfoRepository accountExtraInfoRepository;

    @Override
    public AccountExtraInfoResponse getDetailByAccount(Long accountid) {
        AccountExtraInfo accountExtraInfo = accountExtraInfoRepository.findById(accountid)
                .orElseThrow(() -> new ResourceNotFoundException("No podemos devolver el detalle de la cuenta ["
                        + accountid + "], derivado a que esta no existe."));

        // get current dateTime action...
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        // return model...
        AccountExtraInfoResponse accountExtraInfoResponse = new AccountExtraInfoResponse()
                .builder()
                .accountid(accountExtraInfo.getAccountid())
                .customerId(accountExtraInfo.getCustomerid())
                .customerName(accountExtraInfo.getCustomername())
                .dateTimeResponse(dateFormat.format(date))
                .email(accountExtraInfo.getEmail())
                .interbankaccount(accountExtraInfo.getInterbankaccount())
                .phone(accountExtraInfo.getPhone())
                .rfc(accountExtraInfo.getRfc())
                .msg("Se ha devuelto el detalle de la cuentaID " + accountExtraInfo.getAccountid()
                        + " para que pueda ser registrada.")
                .build();

        // return model..
        return accountExtraInfoResponse;
    }
}
