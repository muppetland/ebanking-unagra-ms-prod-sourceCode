package com.unagra.banking.institutions.api.repository;

import com.unagra.banking.institutions.api.entities.BankingInstitutionsCatalogue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankingInstitutionsCatalogueRepository extends JpaRepository<BankingInstitutionsCatalogue, Long> {

}
