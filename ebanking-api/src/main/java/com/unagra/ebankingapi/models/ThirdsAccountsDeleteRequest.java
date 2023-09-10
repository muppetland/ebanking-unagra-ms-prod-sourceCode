package com.unagra.ebankingapi.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThirdsAccountsDeleteRequest {
	private Long thirdid;
	private Long customerid;
	private String token;

}
