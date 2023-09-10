package com.unagra.auth.jwt.authservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_jwttokenrequest")
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "eventdate")
    private Date eventDate;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "eventdatetime")
    private Date eventdatetime;

    @Column(name = "customerid", nullable = false)
    private Long customerid;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "accesstoken", nullable = true)
    private String accesstoken;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "timesessionexpire", nullable = true)
    private Date timesessionexpire;

    //@OneToOne(mappedBy = "authUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToOne(mappedBy = "authUser")
    private RefreshToken refreshToken;

}
