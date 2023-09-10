package com.unagra.ebankingapi.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThirdsAccountsDTO {
	// adding field of table...
	private Long thirdid;
	private Integer year;
	private Date registrationdate;
	private Date registrationdatetime;
	private Long customerid;
	private String accountcard;
	private String beneficiaryname;
	private String rfc;
	private String email;
	private String telephone;
	private String beneficiarybank;
	private Long institutionid;
	private String nickname;
	private Double limitamount;
	private String istce;
	private Integer isunagra;
	private String statusid;
	private Date lastupdate;
	private String token;
}
