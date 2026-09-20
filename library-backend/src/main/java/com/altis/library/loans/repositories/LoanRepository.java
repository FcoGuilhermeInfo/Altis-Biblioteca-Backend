package com.altis.library.loans.repositories;

import com.altis.library.loans.models.entities.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, UUID> {

    long countByUserIdAndReturnedAtIsNull(UUID userId);

    boolean existsByUserIdAndReturnedAtIsNull(UUID userId);

    List<LoanEntity> findByReturnedAtIsNull();

    List<LoanEntity> findAllByOrderByBorrowedAtDesc();
}
