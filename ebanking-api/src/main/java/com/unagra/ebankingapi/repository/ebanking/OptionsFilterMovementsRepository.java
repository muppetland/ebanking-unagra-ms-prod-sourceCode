package com.unagra.ebankingapi.repository.ebanking;

import com.unagra.ebankingapi.entities.ebanking.OptionsFilterMovements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionsFilterMovementsRepository extends JpaRepository<OptionsFilterMovements, Integer> {
}
