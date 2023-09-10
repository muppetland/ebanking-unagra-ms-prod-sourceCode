package com.unagra.ebankingapi.repository.ebanking;

import com.unagra.ebankingapi.entities.ebanking.AccountExtraInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountExtraInfoRepository extends JpaRepository<AccountExtraInfo, Long> {
    @Query(value = "select subsidiaryid From vw_accountExtraInfo Where accountid = :accountid", nativeQuery = true)
    public Integer findSubsidiaryByAccountID(@Param("accountid") Long accountid);

}
