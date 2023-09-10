package com.unagra.ebankingapi.repository.ebanking;

import com.unagra.ebankingapi.entities.ebanking.ThirdsAccounts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ThirdsAccountsRepository extends JpaRepository<ThirdsAccounts, Long> {
    @Query(value = "SELECT * FROM tbl_thirdsAccounts WHERE customerid=:customerid and accountcard=:account and isUNAGRA=:sourceid", nativeQuery = true)
    public ThirdsAccounts AccountExists(@Param("customerid") Long customerID, @Param("account") String account,
                                        @Param("sourceid") Integer sourceid);

    @Query(value = "SELECT * FROM tbl_thirdsAccounts WHERE customerid=:customerid and isUNAGRA=:sourceid", nativeQuery = true)
    // public List<String> getUNAGRAThirdsAccountsList(@Param("customerid") Long
    // customerID);
    public Page<ThirdsAccounts> getThirdsAccountsList(@Param("customerid") Long customerID,
                                                      @Param("sourceid") Integer sourceid, Pageable pageable);

    @Query(value = "SELECT Count(*) v1 FROM tbl_thirdsAccounts WHERE customerid=:customerid and isUNAGRA=:sourceid", nativeQuery = true)
    public Integer findCustomerThirdsAccounts(@Param("customerid") Long customerID,
                                              @Param("sourceid") Integer sourceid);

    @Query(value = "SELECT Count(*) v1 FROM tbl_thirdsAccounts WHERE thirdid=:thirdid and isUNAGRA=:sourceid", nativeQuery = true)
    public Integer findThirdAccountById(@Param("thirdid") Long thirdid,
                                        @Param("sourceid") Integer sourceid);

    @Query(value = "SELECT customerid FROM tbl_thirdsAccounts WHERE customerid=:customerid and accountcard=:account and isUNAGRA=:sourceid", nativeQuery = true)
    public Long findAccountIDByCustomerID(@Param("customerid") Long customerID, @Param("account") String account,
                                          @Param("sourceid") Integer sourceid);

    @Query(value = "SELECT limitamount FROM tbl_thirdsAccounts WHERE customerid=:customerid and accountcard=:account and isUNAGRA=:sourceid and limitamount<=:amount", nativeQuery = true)
    public Double validateLimitAmount(@Param("customerid") Long customerID, @Param("account") String account,
                                      @Param("sourceid") Integer sourceid, @Param("amount") Double amount);
}
