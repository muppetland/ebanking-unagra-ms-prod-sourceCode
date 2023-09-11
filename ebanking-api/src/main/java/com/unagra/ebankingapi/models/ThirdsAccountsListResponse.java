package com.unagra.ebankingapi.models;

import com.unagra.ebankingapi.entities.ebanking.ThirdsAccounts;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ThirdsAccountsListResponse {
	private Integer pageNo;
	private Integer pageSize;
	private Integer recordsDisplayed;
	private Long recodsTotal;
	private Integer recodsPages;
	private List<ThirdsAccounts> thirdsAccountsList;
	private String dateTimeResponse;
	private String msg;

	public ThirdsAccountsListResponse() {
		thirdsAccountsList = new ArrayList<>();
	}

}
