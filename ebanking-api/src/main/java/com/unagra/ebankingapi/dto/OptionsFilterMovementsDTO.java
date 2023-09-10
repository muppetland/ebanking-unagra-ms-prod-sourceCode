package com.unagra.ebankingapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptionsFilterMovementsDTO {
    private Integer optionID;
    private String description;
    private String filterBy;


}
