package com.unagra.ebankingapi.models;

import com.unagra.ebankingapi.dto.AccountsDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountsByCustomerResponse {
	private Integer accountsTotal;
	private String dateTimeResponse;
	private List<AccountsDTO> accountsDetail;
	private String msg;


	public List<AccountsDTO> getAccountsDetail() {
		return accountsDetail;
	}


}
