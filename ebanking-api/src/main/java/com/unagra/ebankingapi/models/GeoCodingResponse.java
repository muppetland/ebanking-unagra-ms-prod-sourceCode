package com.unagra.ebankingapi.models;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeoCodingResponse {
	private Integer responsecode;
	private Date eventdatetime;
	private Long customerid;
	private String msg;

}
