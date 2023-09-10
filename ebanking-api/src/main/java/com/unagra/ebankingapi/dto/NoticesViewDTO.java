package com.unagra.ebankingapi.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticesViewDTO {
	private Long customerid;
	private Long id;
	private Integer imgid;
	private String msg;
}
