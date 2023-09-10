package com.unagra.ebankingapi.models;

import com.unagra.ebankingapi.dto.MovementsByCustomerIndexDTO;
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
public class MovementsByCustomerIndexResponse {
    private String dateTimeResponse;
    private String msg;
    private List<MovementsByCustomerIndexDTO> movementsDetail;

    public MovementsByCustomerIndexResponse() {
        movementsDetail = new ArrayList<>();
    }
}
