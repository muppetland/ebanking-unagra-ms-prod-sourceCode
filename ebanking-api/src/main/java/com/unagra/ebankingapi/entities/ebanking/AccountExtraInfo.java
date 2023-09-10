package com.unagra.ebankingapi.entities.ebanking;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Immutable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Immutable
@Builder
@Table(name = "vw_accountextrainfo")
public class AccountExtraInfo {
    @Id
    private Long accountid;
    private Long customerid;
    private String interbankaccount;
    private String customername;
    private String rfc;
    private String email;
    private String phone;
    private Integer subsidiaryid;

}
