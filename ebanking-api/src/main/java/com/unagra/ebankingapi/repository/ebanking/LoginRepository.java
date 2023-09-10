package com.unagra.ebankingapi.repository.ebanking;

import com.unagra.ebankingapi.entities.ebanking.Login;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {
    @Query(value = "SELECT * FROM vw_customerLogin WHERE customerid=? and password=?", nativeQuery = true)
    public Login matchPassword(Long customerID, String password);

    @Query(value = "SELECT * FROM vw_customerLogin WHERE customerid=? and email=?", nativeQuery = true)
    public Login matchEmail(Long customerID, String email);

    @Query(value = "Update BancaUNAGRA_PI.dbo.personasFisicas Set Email =?, Clte_Email=? WHERE clienteID=?; Update BancaUNAGRA_PI.dbo.personasMorales Set Email =? WHERE clienteID=?;", nativeQuery = true)
    void updateEmail(String emailD1, String emailD2, Long customerIDPF, String emailD3, Long customerIDPM);

    @Transactional
    @Modifying
    @Query(value = "Update tbl_customers Set password = :passwordParam Where customerID = :customerIDParam", nativeQuery = true)
    void updatePassword(@Param("passwordParam") String password, @Param("customerIDParam") Long customerID);

}
