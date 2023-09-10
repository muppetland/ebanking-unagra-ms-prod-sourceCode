package com.unagra.banking.institutions.api.repository;

import com.unagra.banking.institutions.api.entities.DetailBankInstitution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailBankInstitutionRepository extends JpaRepository<DetailBankInstitution, String> {
}
