package com.unagra.ebankingapi.repository.ebanking;

import com.unagra.ebankingapi.entities.ebanking.OutgoingTransfers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutgoingTransfersRepository extends JpaRepository<OutgoingTransfers, Long> {

}
