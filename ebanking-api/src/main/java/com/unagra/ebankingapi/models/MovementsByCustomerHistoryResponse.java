package com.unagra.ebankingapi.models;

import com.unagra.ebankingapi.entities.bancaUNAGRA.MovementsByCustomer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MovementsByCustomerHistoryResponse {
    private String dateTimeResponse;
    private String msg;
    private Integer pageNo;
    private Integer pageSize;
    private Integer recordsDisplayed;
    private Long recodsTotal;
    private Integer recodsPages;
    private List<MovementsByCustomer> movementsDetail;

    public MovementsByCustomerHistoryResponse() {
        movementsDetail = new ArrayList<>();
    }
}
