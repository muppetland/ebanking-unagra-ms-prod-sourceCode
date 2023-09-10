package com.unagra.ebankingapi.models;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThirdsAccountsUNAGRAResponse {
	private Long thirdID;
	private String origin;
	private String dateTimeResponse;
	private String msg;

}
