package com.unagra.ebankingapi.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutgoingTransfersResponse {
	private String sourceid;
	private String transactiondatetime;
	private String transactionkey;
	private Long transactionid;
	private String msg;

}
