package com.unagra.banking.institutions.api.model;

import com.unagra.banking.institutions.api.entities.BankingInstitutionsCatalogue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class BankingInstitutionsCatalogueResponse {
	private Integer bankingInstitutionsToital;
	private String dateTimeResponse;
	private List<BankingInstitutionsCatalogue> bankingInstitutionsDetail;
	private String msg;

	public BankingInstitutionsCatalogueResponse() {
		bankingInstitutionsDetail = new ArrayList<>();
	}


}
