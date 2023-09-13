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
    private String keymatch;
    private Integer cvespei;
    private String institution;
    private Integer iscard;
}
