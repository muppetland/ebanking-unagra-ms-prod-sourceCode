package com.unagra.ebankingapi.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticesByCustomerResponse {
	private String msg;
	private String dateTimeResponse;
	private Integer noNotice;

}
