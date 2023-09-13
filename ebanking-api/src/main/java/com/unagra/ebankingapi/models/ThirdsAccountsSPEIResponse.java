package com.unagra.ebankingapi.models;

import com.unagra.ebankingapi.dto.DetailBankInstitutionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThirdsAccountsSPEIResponse {
    private Long thirdID;
    private String origin;
    private String dateTimeResponse;
    private String msg;
    //private List<DetailBankInstitutionDTO> detailBank = new ArrayList<>();
    private DetailBankInstitutionDTO detailBank;
}
