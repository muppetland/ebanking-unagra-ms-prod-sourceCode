package com.unagra.ebankingapi.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatchmentMovementsDTO {
	private Long movimientoid;
	private Integer tipomovimientoid;
	private Long inversionid;
	private Long cuentaid;
	private Long clienteid;
	private Integer origenid;
	private Double saldoinicial;
	private Double saldoinicialinversiones;
	private Double monto;
	private Integer monedaid;
	private Integer usuarioid;
	private Integer sucursalid;
	private Integer sucursaldestino;
	private Integer asentado;
	private Date fechamvto;
	private Date fechaaplicacion;
	private String estatusid;
	private Integer isr;
	private String descripcion;
	private Integer sujetoide;
	private Integer intneto;
	private String primaryacctnum;
	private String masteracctnum;
	private String authidresponse;
	private Double impcomision;
	private String terminalid;
	private Integer ta;
	private Long foliooperacion;
	private Long denominacionid;
	private Integer anio;
	private Long transactionid;

}
