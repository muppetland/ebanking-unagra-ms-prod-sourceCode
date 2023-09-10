package com.unagra.ebankingapi.entities.ebanking;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
