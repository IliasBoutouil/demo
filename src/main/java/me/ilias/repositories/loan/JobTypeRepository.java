package me.ilias.repositories.loan;

import me.ilias.domains.loan.JobType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobTypeRepository extends JpaRepository<JobType, Long> {
}