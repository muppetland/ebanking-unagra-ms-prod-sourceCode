package com.unagra.ebankingapi.repository.ebanking;

import com.unagra.ebankingapi.entities.ebanking.TransactionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionKeyRepository extends JpaRepository<TransactionKey, String> {
    // loockup by trackingKey value...
    @Query(value = "SELECT trackingkey FROM tbl_transactionKey WHERE trackingkey=:trackingKey", nativeQuery = true)
    public String TrackingKeyExists(@Param("trackingKey") String trackingKey);

    // loockup by transactionDetailSeal value...
    @Query(value = "SELECT Top 1 DATEDIFF(minute,transactionDateTime,getdate()) v1 FROM tbl_transactionKey WHERE transactionDetailSeal=:transactionDetailSealKey Order by transactionDateTime desc", nativeQuery = true)
    public Integer validateTransactionDetailSeal(@Param("transactionDetailSealKey") String transactionDetailSealKey);

}
