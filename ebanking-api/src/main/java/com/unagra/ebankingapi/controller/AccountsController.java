package com.unagra.ebankingapi.controller;

import com.netflix.discovery.converters.Auto;
import com.unagra.ebankingapi.dto.AccountsArrayResponseDTO;
import com.unagra.ebankingapi.dto.AccountsDTO;
import com.unagra.ebankingapi.entities.ebanking.OptionsFilterMovements;
import com.unagra.ebankingapi.models.AccountExtraInfoResponse;
import com.unagra.ebankingapi.models.AccountsByCustomerResponse;
import com.unagra.ebankingapi.models.MovementsByCustomerHistoryResponse;
import com.unagra.ebankingapi.models.MovementsByCustomerIndexResponse;
import com.unagra.ebankingapi.service.AccountExtraInfoService;
import com.unagra.ebankingapi.service.AccountsService;
import com.unagra.ebankingapi.service.MovementsByCustomerService;
import com.unagra.ebankingapi.service.OptionsFilterMovementsService;
import org.hibernate.query.IllegalQueryOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET})
public class AccountsController {
    @Autowired
    private AccountsService accountsService;

    @Autowired
    private AccountExtraInfoService accountExtraInfoService;

    @Autowired
    private MovementsByCustomerService movementsByCustomerService;

    @Autowired
    private OptionsFilterMovementsService optionsFilterMovementsService;

    @GetMapping("/{customerID}")
    @ResponseStatus(HttpStatus.OK)
    public AccountsArrayResponseDTO getAccountsDetailByCustomer(@PathVariable(name = "customerID") Long customerID) {
        return accountsService.getAccountsByCustomer(customerID);
    }
	/*
	public List<AccountsDTO> getAccountsDetailByCustomer(@PathVariable(name = "customerID") Long customerID) {
		return accountsService.getAccountsByCustomer(customerID);
	}
	 */

    @GetMapping("/detail/{customerID}")
    @ResponseStatus(HttpStatus.OK)
    public AccountsByCustomerResponse getAccountsDetailByCustomerModel(
            @PathVariable(name = "customerID") Long customerID) {
        return accountsService.getAccountsByCustomerModel(customerID);
    }

    @GetMapping("/getAccountExtraInfo/{accountID}")
    @ResponseStatus(HttpStatus.OK)
    public AccountExtraInfoResponse getAccountCustomerName(@PathVariable(name = "accountID") Long accountID) {
        return accountExtraInfoService.getDetailByAccount(accountID);
    }

    @GetMapping("/movements/index")
    @ResponseStatus(HttpStatus.OK)
    public MovementsByCustomerIndexResponse getMovementsByCustomerIndex(@RequestParam(required = true) Long accountID) {
        return movementsByCustomerService.findMovementsByCustomerIndex(accountID);
    }

    @GetMapping("/movements/history")
    @ResponseStatus(HttpStatus.OK)
    public MovementsByCustomerHistoryResponse getMovementsByCustomerHistory(@RequestParam(required = true) Long accountID,
                                                                            @RequestParam(defaultValue = "${ebanking.pageNo}", required = false) Integer pageNo,
                                                                            @RequestParam(defaultValue = "${ebanking.pageSize}", required = false) Integer pageSize,
                                                                            @RequestParam(defaultValue = "${ebanking.filterOption}", required = false) Integer filterOption,
                                                                            @RequestParam(defaultValue = "${ebanking.startDate}", required = false) String startDate,
                                                                            @RequestParam(defaultValue = "${ebanking.endDate}", required = false) String endDate) {
        return movementsByCustomerService.findMovementsByCustomerHistory(accountID, pageNo, pageSize, filterOption, startDate, endDate);
    }

    @GetMapping("/movements/optionsFilter")
    @ResponseStatus(HttpStatus.OK)
    public List<OptionsFilterMovements> getOptionsFilterByMovements() {
        try {
            return optionsFilterMovementsService.getAllOptions();
        } catch (IllegalQueryOperationException ex) {
            throw new IllegalQueryOperationException("¡Ups!, lo sentimos, hay un error en nuestra petición hacía nuestro servidor, paquito esta trabajando en ello para resolver el incoveniente: \n" + ex.getQueryString());
        }
    }
}
