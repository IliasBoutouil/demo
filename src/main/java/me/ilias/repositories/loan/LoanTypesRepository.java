package me.ilias.repositories.loan;

import me.ilias.domains.loan.LoanTypes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanTypesRepository extends JpaRepository<LoanTypes, Long> {
}