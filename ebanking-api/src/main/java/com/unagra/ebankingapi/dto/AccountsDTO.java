package com.unagra.ebankingapi.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountsDTO {
    // adding colums of view...
    private Long customerID;
    private String typeAccount;
    private Long accountID;
    private String typeAccountInterbank;
    private String interbankAccount;
    private String currentBalanceNotice;
    private String currentBalance;
    private String lastMovement;
    private String typeLastMovement;
}
