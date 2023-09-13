package com.unagra.banking.institutions.api.services;


import com.unagra.banking.institutions.api.model.BankingInstitutionsCatalogueResponse;
import com.unagra.banking.institutions.api.model.DetailbyBankingInstitutionResponse;

import java.util.List;

public interface BankingInstitutionsCatalogueService {
	public BankingInstitutionsCatalogueResponse getBankingInstitutiosAvailableArray();

	public DetailbyBankingInstitutionResponse getDetailByBankInstitution (String keymatch);
}
