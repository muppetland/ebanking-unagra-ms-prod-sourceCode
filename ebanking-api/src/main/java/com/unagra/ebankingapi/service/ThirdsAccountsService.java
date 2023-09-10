package com.unagra.ebankingapi.service;

import com.unagra.ebankingapi.dto.ThirdsAccountsDTO;
import com.unagra.ebankingapi.models.ThirdsAccountsDeleteRequest;
import com.unagra.ebankingapi.models.ThirdsAccountsListResponse;
import com.unagra.ebankingapi.models.ThirdsAccountsSPEIResponse;
import com.unagra.ebankingapi.models.ThirdsAccountsUNAGRAResponse;

public interface ThirdsAccountsService {
	public ThirdsAccountsUNAGRAResponse newUNAGRAThirdAccount(ThirdsAccountsDTO thirdsAccountsDTO);

	public ThirdsAccountsSPEIResponse newSPEIThirdAccount(ThirdsAccountsDTO thirdsAccountsDTO);

	public ThirdsAccountsUNAGRAResponse deleteUNAGRAThridAccount(ThirdsAccountsDeleteRequest thirdsAccountsDeleteRequest);

	public ThirdsAccountsUNAGRAResponse deleteSPEIThridAccount(ThirdsAccountsDeleteRequest thirdsAccountsDeleteRequest);

	public ThirdsAccountsListResponse getUNAGRAThirdsAccountsList(Long customerid, Integer pageNo, Integer pageSize);

	public ThirdsAccountsListResponse getSPEIThirdsAccountsList(Long customerid, Integer pageNo, Integer pageSize);

	public ThirdsAccountsUNAGRAResponse updateUNAGRAThridAccount(ThirdsAccountsDTO thirdsAccountsDTO);

	public ThirdsAccountsUNAGRAResponse updateSPEIThridAccount(ThirdsAccountsDTO thirdsAccountsDTO);
}
