package com.unagra.auth.jwt.authservice.repository;

import com.unagra.auth.jwt.authservice.entities.AuthUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Integer> {
    Optional<AuthUser> findByCustomerid(Long customerID);
    //@Query(value = "Select * From tbl_jwtTokenRequest Where customerID=:customerid", nativeQuery = true)
    //public AuthUser findByCustomerID(@Param("customerid") Long customerID);

    @Transactional
    @Modifying
    @Query(value = "Update tbl_jwtTokenRequest Set accessToken =:token, timeSessionExpire=:timeSessionExpire Where customerID =:customerID", nativeQuery = true)
    void updateTokenCustomer(@Param("customerID") Long customerID, @Param("token") String token, @Param("timeSessionExpire") Date timeSessionExpire);
}
