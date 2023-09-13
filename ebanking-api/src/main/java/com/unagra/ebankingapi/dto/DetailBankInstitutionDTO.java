package com.unagra.ebankingapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailBankInstitutionDTO {
    private String keymatch;
    private Integer cvespei;
    private String institution;
    private Integer iscard;
}
