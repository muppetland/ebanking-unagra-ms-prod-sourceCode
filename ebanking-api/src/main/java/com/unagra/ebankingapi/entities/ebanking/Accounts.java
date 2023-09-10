package com.unagra.ebankingapi.entities.ebanking;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Immutable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Immutable
@Builder
@Table(name = "vw_customeraccounts")
public class Accounts {
    @Id
    private Long customerid;
    private String typeaccount;
    private Long accountid;
    private String typeaccountinterbank;
    private String interbankaaccount;
    private String currentbalancenotice;
    private String currentbalance;
    private String lastmovement;
    private String typelastmovement;
}
