package com.unagra.ebankingapi.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
	private Integer responsecode;
	private String eventdatetime;
	private Long customerid;
	private String customerName;
	private String email;
	private String msg;

}
