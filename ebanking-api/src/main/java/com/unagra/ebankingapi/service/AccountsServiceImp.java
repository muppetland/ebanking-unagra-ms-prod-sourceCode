package com.unagra.ebankingapi.service;

import com.unagra.ebankingapi.dto.AccountsArrayResponseDTO;
import com.unagra.ebankingapi.dto.AccountsDTO;
import com.unagra.ebankingapi.entities.ebanking.Accounts;
import com.unagra.ebankingapi.models.AccountsByCustomerResponse;
import com.unagra.ebankingapi.repository.ebanking.AccountsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class AccountsServiceImp implements AccountsService {
    @Autowired
    private AccountsRepository accountsRepository;

    @Override
    public AccountsArrayResponseDTO getAccountsByCustomer(Long customerId) {
        List<String> accountsDetailArray = accountsRepository.findByAccountDetailByCustomerID(customerId);

        //if we don't have information...
        if (accountsDetailArray == null) {
            throw new RuntimeException("¡Ups!, lo sentimos, al parecer no contamos con información relacionada al clienteID [" + customerId + "]");
        }

        //get las access to BEL...
        String vlData = accountsRepository.getLastAccess(customerId);
        String[] vlArrayData = vlData.split(",");

        List<AccountsDTO> finalArray = new ArrayList<>();

        // Fill DTO From Array...
        // for(String accountDetailArray : accountsDetail) {
        for (int i = 0; i < accountsDetailArray.size(); i++) {
            // get current value...
            String vlMainData = accountsDetailArray.get(i);

            // applying split to get individual value of each record...
            String[] vlSplit = vlMainData.split("[,]");

            // currency format...
            Double vlCurrentBalance = Double.parseDouble(vlSplit[6]);

            // Adding to DTO...
            AccountsDTO accountsDTO = new AccountsDTO()
                    .builder()
                    .customerID(Long.parseLong(vlSplit[0]))
                    .typeAccount(vlSplit[1])
                    .accountID(Long.parseLong(vlSplit[2]))
                    .typeAccountInterbank(vlSplit[3])
                    .interbankAccount(vlSplit[4])
                    .currentBalanceNotice(vlSplit[5])
                    .currentBalance(NumberFormat.getCurrencyInstance(new Locale.Builder().setLanguage("en").setRegion("US").build()).format(vlCurrentBalance))
                    .lastMovement(vlSplit[7])
                    .typeLastMovement(vlSplit[8])
                    .build();

            //add to final array....
            finalArray.add(accountsDTO);
        }

        //Generate Response...
        AccountsArrayResponseDTO accountsArrayResponseDTO = new AccountsArrayResponseDTO()
                .builder()
                .accountsDetail(finalArray)
                .lastAccess(vlArrayData[1])
                .sourceDevice(vlArrayData[2])
                .build();
        // List<Accounts> accountsDetail2 = accountsRepository.findAll();
        // System.out.println(accountsDetail.get(0).toString());
        // System.out.println(accountsDetail.get(1).toString());
        // System.out.println(accountsDetail.get(2));
        // loginDetail.stream().map(loginView ->
        // mapToDTO(loginView)).collect(Collectors.toList());
        return accountsArrayResponseDTO;
    }

    @Override
    public AccountsByCustomerResponse getAccountsByCustomerModel(Long customerId) {
        List<String> accountsDetailArray = accountsRepository.findByAccountDetailByCustomerID(customerId);

        //if we don't have information...
        if (accountsDetailArray == null) {
            throw new RuntimeException("¡Ups!, lo sentimos, al parecer no contamos con información relacionada al clienteID [" + customerId + "]");
        }

        List<AccountsDTO> finalArray = new ArrayList<>();

        // create model to return data...
        AccountsByCustomerResponse accountByCustomer = new AccountsByCustomerResponse();

        // Fill DTO From Array...
        // for(String accountDetailArray : accountsDetail) {
        for (int i = 0; i < accountsDetailArray.size(); i++) {
            // get current value...
            String vlMainData = accountsDetailArray.get(i);

            // applying split to get individual value of each record...
            String[] vlSplit = vlMainData.split("[,]");

            // currency format...
            Double vlCurrentBalance = Double.parseDouble(vlSplit[6]);

            // Adding to DTO...
            AccountsDTO accountsDTO = new AccountsDTO().builder().customerID(Long.parseLong(vlSplit[0])).typeAccount(vlSplit[1]).accountID(Long.parseLong(vlSplit[2])).typeAccount(vlSplit[3]).interbankAccount(vlSplit[4]).currentBalanceNotice(vlSplit[5]).currentBalance(NumberFormat.getCurrencyInstance(new Locale.Builder().setLanguage("en").setRegion("US").build()).format(vlCurrentBalance)).lastMovement(vlSplit[7]).typeLastMovement(vlSplit[8]).build();

            //add to final array....
            finalArray.add(accountsDTO);
        }

        // get current dateTime action...
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        // return model with data...
        accountByCustomer.setAccountsTotal(accountsDetailArray.size());
        accountByCustomer.setDateTimeResponse(dateFormat.format(date));
        accountByCustomer.setMsg("La cuenta [" + customerId.toString() + "] ha devuelto un total de " + accountsDetailArray.size() + " cueta(s) asociada(s).");
        accountByCustomer.setAccountsDetail(finalArray);

        // List<Accounts> accountsDetail2 = accountsRepository.findAll();
        // System.out.println(accountsDetail.get(0).toString());
        // System.out.println(accountsDetail.get(1).toString());
        // System.out.println(accountsDetail.get(2));
        // loginDetail.stream().map(loginView ->
        // mapToDTO(loginView)).collect(Collectors.toList());
        return accountByCustomer;
    }

    // convert Entity to DTO...
    private AccountsDTO mapAccountsToDTO(Accounts accounts) {
        AccountsDTO accountsDTO = new AccountsDTO()
                .builder()
                .customerID(accounts.getCustomerid())
                .typeAccount(accounts.getTypeaccount())
                .accountID(accounts.getAccountid())
                .typeAccountInterbank(accounts.getTypeaccountinterbank())
                .interbankAccount(accounts.getInterbankaaccount())
                .currentBalanceNotice(accounts.getCurrentbalancenotice())
                .currentBalance(accounts.getCurrentbalance())
                .lastMovement(accounts.getLastmovement())
                .typeLastMovement(accounts.getTypelastmovement())
                .build();
        return accountsDTO;
    }

    // convert DTO to Entity...
    private Accounts mapAccountsToEntity(AccountsDTO accountsDTO) {
        Accounts accounts = new Accounts()
                .builder()
                .customerid(accountsDTO.getCustomerID())
                .typeaccount(accountsDTO.getTypeAccount())
                .accountid(accountsDTO.getAccountID())
                .typeaccountinterbank(accountsDTO.getTypeAccountInterbank())
                .interbankaaccount(accountsDTO.getInterbankAccount())
                .currentbalancenotice(accountsDTO.getCurrentBalanceNotice())
                .currentbalance(accountsDTO.getCurrentBalance())
                .lastmovement(accountsDTO.getLastMovement())
                .typelastmovement(accountsDTO.getTypeLastMovement())
                .build();
        return accounts;
    }

}
