package com.unagra.ebankingapi.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankingInstitutionsDTO {
	// adding fields of view...
	private Long id;
	private String institution;

}
