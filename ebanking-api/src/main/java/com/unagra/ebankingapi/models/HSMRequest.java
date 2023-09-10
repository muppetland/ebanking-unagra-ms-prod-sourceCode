package com.unagra.ebankingapi.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HSMRequest {
	private Long customerid;
	private String otp;
	private Integer moduleid;

}
