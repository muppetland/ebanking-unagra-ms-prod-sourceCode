package com.unagra.ebankingapi.entities.ebanking;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "tbl_geocoding")
public class GeoCoding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "geocodingid", nullable = false)
    private Long geoCodingId;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "eventdate", nullable = false)
    private Date eventDate;

    // @Temporal(value = TemporalType.TIMESTAMP)
    // @Column(name = "eventdateTime", nullable = false)
    // private java.sql.Timestamp eventDateTime;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "eventdatetime", nullable = false)
    private Date eventDateTime;

    @Column(name = "customerid", nullable = false)
    private Long customerId;

    @Column(name = "tokenrequest", nullable = false)
    private String tokenRequest;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "locality", nullable = false)
    private String locality;

    @Column(name = "postalcode", nullable = false)
    private String postalCode;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "lat", nullable = false)
    private String lat;

    @Column(name = "lng", nullable = false)
    private String lng;

    @Column(name = "placeid", nullable = false)
    private String placeId;

}
