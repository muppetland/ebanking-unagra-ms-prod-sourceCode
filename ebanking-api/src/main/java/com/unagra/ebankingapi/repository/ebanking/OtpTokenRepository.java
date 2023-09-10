package com.unagra.ebankingapi.repository.ebanking;

import com.unagra.ebankingapi.entities.ebanking.OtpToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
    @Query(value = "Select Trim(token) v1 From eBanking.dbo.tbl_otpTokenRequest Where token = :token", nativeQuery = true)
    public String findByToken(@Param("token") String token);

    @Query(value = "Select Trim(token) v1 From eBanking.dbo.tbl_otpTokenRequest Where customerId = :customerid and token = :token and statusid = 'v'", nativeQuery = true)
    public String findTokenByCustomer(@Param("customerid") Long customerid, @Param("token") String token);

    @Transactional
    @Modifying
    @Query(value = "Update eBanking.dbo.tbl_otpTokenRequest Set statusid = 'F' Where customerId = :customerid and token = :token", nativeQuery = true)
    void updateStatusTokenByCustomer(@Param("customerid") Long customerid, @Param("token") String token);

}
