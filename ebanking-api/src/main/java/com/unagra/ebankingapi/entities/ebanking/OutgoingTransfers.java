package com.unagra.ebankingapi.entities.ebanking;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "tbl_outgoingtransfers")
public class OutgoingTransfers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transferid", nullable = false)
    private Long transferid;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "transactiondate", nullable = false)
    private Date transactiondate;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "transactiondateTime", nullable = false)
    private Date transactiondatetime;

    @Column(name = "enviormentid", nullable = false)
    private Integer enviormentid; // to know if current transaction was created in BIU or other device...

    @Column(name = "sourceid", nullable = false, columnDefinition = "integer default 1")
    private Integer sourceid; // to know if current transactions belogns to SPEI or UNAGRA...

    @Column(name = "chargemovementid", nullable = false)
    private Long chargemovementid;

    @Column(name = "paymentmovementid", nullable = false)
    private Long paymentmovementid;

    @Column(name = "catchmentfolio", nullable = false)
    private Long catchmentfolio;

    @Column(name = "customerid_origin", nullable = false)
    private Long customerid_origin;

    @Column(name = "accountid_origin", nullable = false)
    private Long accountid_origin;

    @Column(name = "interbankkey", nullable = false)
    private String interbankkey;

    @Column(name = "customerid_destination", nullable = false, columnDefinition = "bigint default 0")
    private Long customerid_destination;

    @Column(name = "accountid_destination", nullable = false, columnDefinition = "bigint default 0")
    private Long accountid_destination;

    @Column(name = "destinationaccountcard", nullable = false, columnDefinition = "varchar(18) default ''")
    private String destinationaccountcard;

    @Column(name = "concept", nullable = false, columnDefinition = "varchar(40) default ''")
    private String concept;

    @Column(name = "amount", nullable = false, columnDefinition = "decimal(19,2) default 0")
    private Double amount;

    @Column(name = "institutionid", nullable = false, columnDefinition = "integer default 90656")
    private Integer institutionid;

    @Column(name = "numericalreference", nullable = false, columnDefinition = "bigint default 0")
    private Long numericalreference;

    @Column(name = "trackingkey", nullable = true, columnDefinition = "varchar(30) default ''")
    private String trackingkey;

    @Column(name = "originalstring", nullable = true, columnDefinition = "varchar(max) default ''")
    private String originalstring;

    @Column(name = "digitalseal", nullable = true, columnDefinition = "varchar(max) default ''")
    private String digitalseal;

    @Column(name = "commissionid", nullable = false, columnDefinition = "integer default 1")
    private Integer commissionid;

    @Column(name = "transactionkey", nullable = false, columnDefinition = "varchar(max) default ''")
    private String transactionkey;

    @Column(name = "geocodingid", nullable = false, columnDefinition = "bigint default 0")
    private Long geocodingid;

}
