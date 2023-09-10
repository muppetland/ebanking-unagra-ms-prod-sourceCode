package com.unagra.ebankingapi.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticesByCustomerDTO {
	private Long id;
	private Long customerid;
}
