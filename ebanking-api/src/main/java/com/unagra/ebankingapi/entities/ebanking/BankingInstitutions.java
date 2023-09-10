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
@Table(name = "vw_bankinginstitutionscatalogue")
public class BankingInstitutions {
    // adding fields of view...
    @Id
    private Long id;
    private String institution;
}
