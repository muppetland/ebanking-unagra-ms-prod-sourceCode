package com.unagra.ebankingapi.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountExtraInfoResponse {
	private Long accountid;
	private Long customerId;
	private String interbankaccount;
	private String customerName;
	private String rfc;
	private String email;
	private String phone;
	private String dateTimeResponse;
	private String msg;
}
