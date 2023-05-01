package me.ilias.repositories.loan;

import me.ilias.domains.loan.LoanRateParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface LoanRateParamRepository extends JpaRepository<LoanRateParam, Long> {

    //ALTER SEQUENCE seq RESTART WITH 1;
    @Modifying
    @Query(value = "ALTER TABLE loan_rate_param ALTER COLUMN id RESTART WITH 1",nativeQuery = true)
      void resetIdentity();
}