package com.unagra.ebankingapi.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogsResponse {
	private Integer responsecode;
	private String eventdatetime;
	private Integer enviorment;
	private Long customerid;
	private Integer module;
	private String action;
	private String msg;

}
