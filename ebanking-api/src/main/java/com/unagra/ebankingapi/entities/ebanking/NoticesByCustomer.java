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
@Table(name = "tbl_noticecustomer")
public class NoticesByCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "noticeCatalogid", nullable = false)
    private Integer noticeCatalogid;

    @Column(name = "customerid", nullable = false)
    private Long customerid;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "noticedatetimeattended", nullable = true)
    private Date noticedatetimeattended;

    @Column(name = "statusid", nullable = false)
    private String statusid;

}
