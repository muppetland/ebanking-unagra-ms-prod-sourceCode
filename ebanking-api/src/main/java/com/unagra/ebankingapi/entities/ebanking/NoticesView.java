package com.unagra.ebankingapi.entities.ebanking;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Immutable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Immutable
@Table(name = "vw_notices")
public class NoticesView {
    @Id
    @Column(name = "customerid", nullable = false)
    private Long customerid;

    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "imgid", nullable = false)
    private Integer imgid;

    @Column(name = "msg", nullable = false)
    private String msg;

}
