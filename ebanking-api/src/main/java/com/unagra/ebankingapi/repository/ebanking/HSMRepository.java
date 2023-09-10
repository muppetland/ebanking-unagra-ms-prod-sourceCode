package com.unagra.ebankingapi.repository.ebanking;

import com.unagra.ebankingapi.entities.ebanking.GetValuesParams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HSMRepository extends JpaRepository<GetValuesParams, Long> {
    @Query(value = "Select Trim(urlProduccion) v1 From Parametros.dbo.tbl_endpoints Where class = :className and opt = :opt", nativeQuery = true)
    public String getURLEnviorment(@Param("className") String className, @Param("opt") Integer opt);

}
