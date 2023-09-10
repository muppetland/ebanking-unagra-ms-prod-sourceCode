package com.unagra.ebankingapi.repository.ebanking;

import com.unagra.ebankingapi.entities.ebanking.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {
    @Query(value = "select * From vw_customerAccounts Where customerID = :customerid", nativeQuery = true)
    List<String> findByAccountDetailByCustomerID(@Param("customerid") Long customerid);

    @Query(value = "select accountid From vw_customerAccounts Where accountid = :accountid", nativeQuery = true)
    public Long findAccountID(@Param("accountid") Long accountid);

    @Query(value = "select currentBalance From vw_customerAccounts Where accountid = :accountid", nativeQuery = true)
    public Double getBalanceByAccountID(@Param("accountid") Long accountid);

    @Query(value = "EXEC eBanking.dbo.sp_getCatchmentFolio @vpSource=:sourceid", nativeQuery = true)
    public Long getCatchmentFolio(@Param("sourceid") Long sourceid);

    @Query(value = "select * From vw_lastAccess Where customerID = :customerid", nativeQuery = true)
    String getLastAccess(@Param("customerid") Long customerid);
}
