package com.unagra.ebankingapi.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionKeyDTO {
	private String transactionkeyId;
	private Integer year;
	private Date transactiondate;
	private Date transactiondatetime;
	private String trackingkey;
	private Long catchmentfolio;
	private String transactiondetailseal;
}
