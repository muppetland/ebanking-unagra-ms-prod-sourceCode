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
@Table(name = "tbl_otptokenrequest")
public class OtpToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "eventdate", nullable = false)
    private Date eventdate;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "eventdatetime", nullable = false)
    private Date eventdatetime;

    @Column(name = "customerid", nullable = false)
    private Long customerid;

    @Column(name = "moduleid", nullable = false)
    private Integer moduleid;

    @Column(name = "otp", nullable = false)
    private String otp;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "statusid", nullable = false)
    private String statusid;

}
