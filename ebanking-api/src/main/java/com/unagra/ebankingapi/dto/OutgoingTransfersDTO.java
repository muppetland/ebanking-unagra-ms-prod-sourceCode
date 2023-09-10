package com.unagra.ebankingapi.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutgoingTransfersDTO {
	private Long transferid;
	private Integer year;
	private Date transactiondate;
	private Date transactiondatetime;
	private Integer enviormentid; // to know if current transaction was created in BIU or other device...
	private Integer sourceid; // to know if current transactions belogns to SPEI or UNAGRA...
	private Long chargemovementid;
	private Long paymentmovementid;
	private Long catchmentfolio;
	private Long customerid_origin;
	private Long accountid_origin;
	private String interbankkey;
	private Long customerid_destination;
	private Long accountid_destination;
	private String destinationaccountcard;
	private String concept;
	private Double amount;
	private Integer institutionid;
	private Long numericalreference;
	private String trackingkey;
	private String originalstring;
	private String digitalseal;
	private Integer commissionid;
	private String transactionkey;
	private Long geocodingid;
	private String token;

}
