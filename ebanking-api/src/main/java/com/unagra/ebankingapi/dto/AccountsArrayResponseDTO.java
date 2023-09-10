package com.unagra.ebankingapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class AccountsArrayResponseDTO {
    private String lastAccess;
    private String sourceDevice;
    private List<AccountsDTO> accountsDetail;

    public AccountsArrayResponseDTO() {
        accountsDetail = new ArrayList<>();
    }
}
