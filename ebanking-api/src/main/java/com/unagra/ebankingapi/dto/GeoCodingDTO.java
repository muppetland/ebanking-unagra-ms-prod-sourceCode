package com.unagra.ebankingapi.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeoCodingDTO {
	// adding columns of table...
	private Long geoCodingId;
	private Integer year;
	private Date eventDate;
	private Date eventDateTime;
	private Long customerId;
	private String tokenRequest;
	private String country;
	private String locality;
	private String postalCode;
	private String address;
	private String lat;
	private String lng;
	private String placeId;

}
