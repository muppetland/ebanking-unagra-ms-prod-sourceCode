package com.unagra.ebankingapi.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordRecoveredRequest {
	private String email;
	private String password;
	private String token;

}
