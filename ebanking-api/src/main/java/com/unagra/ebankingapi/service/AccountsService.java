package com.unagra.ebankingapi.service;

import java.util.List;

import com.unagra.ebankingapi.dto.AccountsArrayResponseDTO;
import com.unagra.ebankingapi.dto.AccountsDTO;
import com.unagra.ebankingapi.models.AccountsByCustomerResponse;

public interface AccountsService {
	//public List<AccountsDTO> getAccountsByCustomer(Long customerId);
	public AccountsArrayResponseDTO getAccountsByCustomer(Long customerId);

	public AccountsByCustomerResponse getAccountsByCustomerModel(Long customerId);
	
}
