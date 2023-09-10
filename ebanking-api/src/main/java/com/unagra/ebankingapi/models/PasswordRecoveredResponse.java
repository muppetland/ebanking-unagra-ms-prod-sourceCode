package com.unagra.ebankingapi.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordRecoveredResponse {
	private Integer responsecode;
	private String eventdatetime;
	private Long customerid;
	private String msg;

}
