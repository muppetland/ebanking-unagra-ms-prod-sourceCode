package com.unagra.ebankingapi.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HSMResponse {
	private Integer responsecode;
	private String eventdatetime;
	private Long customerid;
	private String token;
	private String msg;

}
