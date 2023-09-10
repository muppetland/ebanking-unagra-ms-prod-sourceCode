package com.unagra.ebankingapi.entities.ebanking;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Immutable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Immutable
@Table(name = "getValuesTemp")
public class GetValuesParams {
    @Id
    @Column(name = "id", nullable = true)
    private Long id;

    @Column(name = "v1", nullable = true)
    private String v1;

}
