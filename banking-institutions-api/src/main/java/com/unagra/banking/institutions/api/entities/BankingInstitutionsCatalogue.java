package com.unagra.banking.institutions.api.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Immutable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Immutable
@Table(name = "vw_bankinginstitutionscatalogue")
public class BankingInstitutionsCatalogue {
    // adding fields of view...
    @Id
    private Long id;
    private String institution;
}
