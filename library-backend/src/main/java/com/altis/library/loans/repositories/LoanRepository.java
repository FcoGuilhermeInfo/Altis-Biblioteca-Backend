package com.altis.library.loans.repositories;

import com.altis.library.loans.models.entities.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, UUID> {

    long countByUserIdAndReturnedAtIsNull(UUID userId);

    boolean existsByUserIdAndReturnedAtIsNull(UUID userId);

    boolean existsByBookId(UUID bookId);

    List<LoanEntity> findByReturnedAtIsNull();

    List<LoanEntity> findAllByOrderByBorrowedAtDesc();

    @Query("""
            select loan from LoanEntity loan
            where lower(loan.book.title) like lower(concat('%', :search, '%'))
               or lower(loan.user.name) like lower(concat('%', :search, '%'))
            order by loan.borrowedAt desc
            """)
    Page<LoanEntity> search(@Param("search") String search, Pageable pageable);

    Page<LoanEntity> findAllByOrderByBorrowedAtDesc(Pageable pageable);
}
