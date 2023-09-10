package com.unagra.ebankingapi.entities.ebanking;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_optionsfiltermovements")
public class OptionsFilterMovements {
    @Id
    @JsonProperty("optionID")
    @Column(name = "optionid")
    private Integer optionID;

    @Column(name = "description")
    private String description;

    @JsonProperty("filterBy")
    @Column(name = "filterby")
    private String filterBy;
}
