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
@Table(name = "vw_customerlogin")
public class Login {
    @Id
    private Long customerid;
    private String customername;
    private String email;
    private String password;
    private Integer requestchangepassword;
    private Integer locked;
    private String statusid;
    private Integer failedattempts;

}
