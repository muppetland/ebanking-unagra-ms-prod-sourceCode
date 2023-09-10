package com.unagra.ebankingapi.repository.ebanking;

import com.unagra.ebankingapi.entities.ebanking.NoticesView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NoticesRepository extends JpaRepository<NoticesView, Long> {
    @Query(value = "Select * From vw_notices Where customerID =:customerID", nativeQuery = true)
    public List<String> findAllNoticesByCustomer(@Param("customerID") Long customerID);

}
