package com.unagra.banking.institutions.api.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Immutable
@Table(name = "vw_detailbybankinstitution")
public class DetailBankInstitution {
    @Id
    @Column(name = "keymatch", nullable = false)
    private String keymatch;

    @Column(name = "cvespei", nullable = false)
    private Integer cvespei;

    @Column(name = "institution", nullable = false)
    private String institution;

    @Column(name = "iscard", nullable = false)
    private Integer iscard;
}
