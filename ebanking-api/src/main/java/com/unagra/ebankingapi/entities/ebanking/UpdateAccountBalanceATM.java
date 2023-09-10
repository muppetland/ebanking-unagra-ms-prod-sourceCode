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
@Table(name = "tbl_updateaccountbalanceatm")
public class UpdateAccountBalanceATM {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "", nullable = false)
    private Long id;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "transactiondate")
    private Date transactiondate;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "trasactionDateTime", nullable = false)
    private Date trasactiondatetime;

    @Column(name = "accountid", nullable = false)
    private Long accountid;

    @Column(name = "customerid", nullable = false)
    private Long customerid;

    @Column(name = "proccessed", nullable = false)
    private Integer proccessed;
}
