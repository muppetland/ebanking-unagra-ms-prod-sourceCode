package com.unagra.ebankingapi.models;

import com.unagra.ebankingapi.entities.ebanking.BankingInstitutions;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BankingInstitutionsResponse {
	private Integer bankingInstitutionsToital;
	private String dateTimeResponse;
	private List<BankingInstitutions> bankingInstitutionsDetail;
	private String msg;

	public BankingInstitutionsResponse() {
		bankingInstitutionsDetail = new ArrayList<>();
	}


}
