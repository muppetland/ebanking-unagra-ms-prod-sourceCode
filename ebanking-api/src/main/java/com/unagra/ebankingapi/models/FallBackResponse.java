package com.unagra.ebankingapi.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FallBackResponse {
    private String dateTime;
    private Integer responseCode;
    private String msg;
    private String exception;
}
