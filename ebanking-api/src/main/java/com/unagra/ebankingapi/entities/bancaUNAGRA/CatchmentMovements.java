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
@Table(name = "tbl_movimientoscaptacion")
public class CatchmentMovements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movimientoid", nullable = false)
    private Long movimientoid;

    @Column(name = "tipomovimientoid", nullable = false)
    private Integer tipomovimientoid;

    @Column(name = "inversionid", nullable = true)
    private Long inversionid;

    @Column(name = "cuentaid", nullable = false)
    private Long cuentaid;

    @Column(name = "clienteid", nullable = false)
    private Long clienteid;

    @Column(name = "origenid", nullable = true)
    private Integer origenid;

    @Column(name = "saldoinicial", nullable = true)
    private Double saldoinicial;

    @Column(name = "saldoinicialinversiones", nullable = true)
    private Double saldoinicialinversiones;

    @Column(name = "monto", nullable = true)
    private Double monto;

    @Column(name = "monedaid", nullable = true)
    private Integer monedaid;

    @Column(name = "usuarioid", nullable = true)
    private Integer usuarioid;

    @Column(name = "sucursalid", nullable = true)
    private Integer sucursalid;

    @Column(name = "sucursaldestino", nullable = true)
    private Integer sucursaldestino;

    @Column(name = "asentado", nullable = true)
    private Integer asentado;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "fechamvto", nullable = true)
    private Date fechamvto;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "fechaaplicacion", nullable = true)
    private Date fechaaplicacion;

    @Column(name = "estatusid", nullable = true)
    private String estatusid;

    @Column(name = "isr", nullable = true)
    private Integer isr;

    @Column(name = "descripcion", nullable = true)
    private String descripcion;

    @Column(name = "sujetoide", nullable = true)
    private Integer sujetoide;

    @Column(name = "intneto", nullable = false)
    private Integer intneto;

    @Column(name = "primaryacctnum", nullable = true)
    private String primaryacctnum;

    @Column(name = "masteracctnum", nullable = true)
    private String masteracctnum;

    @Column(name = "authidresponse", nullable = true)
    private String authidresponse;

    @Column(name = "impcomision", nullable = true)
    private Double impcomision;

    @Column(name = "terminalid", nullable = true)
    private String terminalid;

    @Column(name = "ta", nullable = true)
    private Integer ta;

    @Column(name = "foliooperacion", nullable = false)
    private Long foliooperacion;

    @Column(name = "denominacionid", nullable = true)
    private Long denominacionid;

    @Column(name = "anio", nullable = false)
    private Integer anio;

    @Column(name = "transactionid", nullable = false, columnDefinition = "bigint default 0")
    private Long transactionid;
}
