package com.unagra.ebankingapi.repository.bancaUNAGRA;

import com.unagra.ebankingapi.entities.bancaUNAGRA.MovementsByCustomer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
public interface MovementsByCustomerRepository extends JpaRepository<MovementsByCustomer, Long> {
    @Query(value = "EXEC eBanking.dbo.sp_movementsByCustomerIndex @vpAccountID=:accountid", nativeQuery = true)
    public List<String> findMovementsByCustomerIndex(@Param("accountid") Long cuentaid);

    @Query(value = "Select * From eBanking.dbo.vw_movementsByCustomerHistory Where cuentaid =:accountid " +
            " and Cast(fechaaplicacion as date) between :startDate and :endDate and " +
            " tipomovimientoid in (SELECT Split.a.value('.', 'VARCHAR(100)') AS Data  FROM  " +
            "(SELECT CAST ('<M>' + REPLACE(filterBy, ',', '</M><M>') + '</M>' AS XML) AS Data  FROM " +
            " eBanking.dbo.tbl_optionsFilterMovements Where optionid = :optionID) AS A " +
            "CROSS APPLY Data.nodes ('/M') AS Split(a))", nativeQuery = true)
    public Page<MovementsByCustomer> findMovementsByCustomerHistory(@Param("accountid") Long accountid, @Param("optionID") Integer optionID, @Param("startDate") Date startDate, @Param("endDate") Date endDate, Pageable pageable);
}
