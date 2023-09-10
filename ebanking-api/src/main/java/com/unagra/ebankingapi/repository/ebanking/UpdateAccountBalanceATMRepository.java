package com.unagra.ebankingapi.repository.ebanking;

import com.unagra.ebankingapi.entities.ebanking.UpdateAccountBalanceATM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpdateAccountBalanceATMRepository extends JpaRepository<UpdateAccountBalanceATM, Long> {
}
