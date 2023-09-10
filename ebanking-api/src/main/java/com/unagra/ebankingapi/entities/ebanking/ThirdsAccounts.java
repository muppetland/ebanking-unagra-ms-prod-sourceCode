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
@Table(name = "tbl_thirdsaccounts")
public class ThirdsAccounts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "thirdid", nullable = false)
    private Long thirdid;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "registrationdate", nullable = false)
    private Date registrationdate;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "registrationdatetime", nullable = false)
    private Date registrationdatetime;

    @Column(name = "customerid", nullable = false)
    private Long customerid;

    @Column(name = "accountcard", nullable = false)
    private String accountcard;

    @Column(name = "beneficiaryname", nullable = true)
    private String beneficiaryname;

    @Column(name = "rfc", nullable = true)
    private String rfc;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "telephone", nullable = true)
    private String telephone;

    @Column(name = "beneficiarybank", nullable = false)
    private String beneficiarybank;

    @Column(name = "institutionid", nullable = false)
    private Long institutionid;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "limitamount", nullable = false)
    private Double limitamount;

    @Column(name = "istce", nullable = false)
    private String istce;

    @Column(name = "isunagra", nullable = false)
    private Integer isunagra;

    @Column(name = "statusid", nullable = false)
    private String statusid;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "lastupdate", nullable = true)
    private Date lastupdate;

}
