package com.unagra.ebankingapi.entities.bancaUNAGRA;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Immutable;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Immutable
@Table(name = "vw_movementsbycustomerhistory")
public class MovementsByCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movimientoid")
    private Long movimientoid;

    @Column(name = "cuentaid")
    private Long cuentaid;

    @Column(name = "tipomovimiento")
    private String tipomovimiento;

    @Column(name = "origenmovimiento")
    private String origenmovimiento;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "fechahoramovimiento")
    private Date fechahoramovimiento;

    @Column(name = "monto")
    private String monto;

    @Column(name = "concepto")
    private String concepto;

    @Column(name = "tipooperacion")
    private String tipooperacion;

    @Column(name = "tipomovimientoid")
    private Integer tipomovimientoid;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "fechaaplicacion")
    private Date fechaaplicacion;
}
