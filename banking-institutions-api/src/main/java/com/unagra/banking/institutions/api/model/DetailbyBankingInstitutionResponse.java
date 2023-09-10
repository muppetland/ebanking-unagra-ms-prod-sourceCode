package com.unagra.banking.institutions.api.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailbyBankingInstitutionResponse {
    private String keymatch;
    private Integer cvespei;
    private String institution;
    private Integer iscard;
}
