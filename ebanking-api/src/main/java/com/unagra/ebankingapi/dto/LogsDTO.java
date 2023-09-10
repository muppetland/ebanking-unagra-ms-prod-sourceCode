package com.unagra.ebankingapi.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogsDTO {
	// adding columns of table...
	private Long id;
	private Integer year;
	private Date eventdate;
	private Date eventdatetime;
	private Integer enviormentid;
	private Long customerid;
	private Integer moduleid;
	private String action;
	private String publicip;
	private String localip;
	private String device;
	private Long geocodingid;


}
