package com.unagra.auth.jwt.authservice.repository;

import com.unagra.auth.jwt.authservice.entities.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    public Optional<RefreshToken> findByRefreshtoken(String refreshToken);
    //@Transactional
    //@Query(value = "Select * From tbl_jwtTokenRefresh Where refreshToken=:refreshtoken", nativeQuery = true)
    //public RefreshToken findByRefreshToken(@Param("refreshtoken") String refreshToken);

    @Modifying
    @Transactional
    @Query(value = "Delete From tbl_jwtTokenRefresh Where userTokenID=:usertokenid", nativeQuery = true)
    void deleteUserTokenID(@Param("usertokenid") Long refreshToken);

}
