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
@Table(name = "tbl_activitylog")
public class Logs {
    // adding fieds of table...
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "eventdate", nullable = false)
    private Date eventdate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "eventdatetime", nullable = false)
    private Date eventdatetime;

    @Column(name = "enviormentid", nullable = false)
    private Integer enviormentid;

    @Column(name = "customerid", nullable = false)
    private Long customerid;

    @Column(name = "moduleid", nullable = false)
    private Integer moduleid;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "publicip", nullable = true)
    private String publicip;

    @Column(name = "localip", nullable = true)
    private String localip;

    @Column(name = "device", nullable = false)
    private String device;

    @Column(name = "geocodingid", nullable = true)
    private Long geocodingid;

}
