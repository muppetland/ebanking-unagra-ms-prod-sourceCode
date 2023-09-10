package com.unagra.ebankingapi.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionKeyResponse {
	private String transactionKey;
	private String trackinigKey;
	private Long catchmentfolio;
	private String dateTimeResponse;
	private String msg;

}
