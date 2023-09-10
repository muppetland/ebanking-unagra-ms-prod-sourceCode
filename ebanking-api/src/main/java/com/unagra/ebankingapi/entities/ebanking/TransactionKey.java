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
@Table(name = "tbl_transactionkey")
public class TransactionKey {
    @Id
    @Column(name = "transactionkeyid", nullable = false)
    private String transactionkeyid;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "transactiondate", nullable = false)
    private Date transactiondate;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "transactiondatetime", nullable = false)
    private Date transactiondatetime;

    @Column(name = "trackingkey", nullable = true)
    private String trackingkey;

    @Column(name = "transactiondetailseal", nullable = false)
    private String transactiondetailseal;

}
