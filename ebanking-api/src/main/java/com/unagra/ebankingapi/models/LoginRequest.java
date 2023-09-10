package com.unagra.ebankingapi.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
	private Long customerid;
	private String password;

}
