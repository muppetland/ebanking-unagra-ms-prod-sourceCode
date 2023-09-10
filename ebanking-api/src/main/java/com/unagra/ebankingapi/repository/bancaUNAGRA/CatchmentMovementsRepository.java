package com.unagra.ebankingapi.repository.bancaUNAGRA;

import com.unagra.ebankingapi.entities.bancaUNAGRA.CatchmentMovements;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CatchmentMovementsRepository extends JpaRepository<CatchmentMovements, Long> {
    @Query(value = "Select Trim() v1 From BancaUNAGRA_PI.dbo.TipoMovimiento Where tipoMovimientoID = :tipomovimientoid", nativeQuery = true)
    public String getMovementDescription(@Param("tipomovimientoid") String tipomovimientoid);

    @Transactional
    @Modifying
    @Query(value = "Update BancaUnagra_PI.dbo.SaldosCuentaEje Set SaldoFinal = (SaldoFinal - :amount) Where Cast(dia as date) >= Cast(getdate() as date()) and CuentaID = :accountid and EstatusID = 'v'", nativeQuery = true)
    public String updateBalanceBIU(@Param("amount") Double amount, @Param("accountid") Long accountid);
}
