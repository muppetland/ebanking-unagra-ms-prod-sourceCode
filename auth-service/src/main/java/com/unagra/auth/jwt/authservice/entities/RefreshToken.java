package com.unagra.auth.jwt.authservice.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "tbl_jwttokenrefresh")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "refreshtoken")
    private String refreshtoken;

    @Column(name = "timesessionexpire")
    private Instant timesessionexpire;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usertokenid", referencedColumnName = "id")
    //@JoinColumn(name = "usertokenid", referencedColumnName = "id")
    //@JoinColumn(name = "usertokenid", nullable = false)
    private AuthUser authUser;

}
