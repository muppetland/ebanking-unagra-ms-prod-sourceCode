package com.unagra.ebankingapi.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDTO {

	// adding columns of view...
	private Long customerid;
	private String customername;
	private String email;
	private String password;
	private Integer requestchangepassword;
	private Integer locked;
	private String statusId;
	private Integer failedAttempts;

}
