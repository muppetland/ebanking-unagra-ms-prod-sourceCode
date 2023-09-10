package com.unagra.ebankingapi.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovementsByCustomerHistoryDTO {
    private Long movimientoid;
    private Long cuentaid;
    private String tipomovimiento;
    private String origenmovimiento;
    private String fechahoramovimiento;
    private String monto;
    private String concepto;
    private String tipooperacion;
    private Integer tipomovimientoid;
    private String fechaaplicacion;
}

