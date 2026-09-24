package com.altis.library.loans.repositories;

import com.altis.library.loans.models.entities.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDateTime;
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
            where (
                    :search = ''
                 or lower(loan.book.title) like lower(concat('%', :search, '%'))
                 or lower(loan.user.name) like lower(concat('%', :search, '%'))
              )
            order by
                case
                    when loan.status = com.altis.library.loans.models.enums.LoanStatus.OVERDUE then 0
                    when loan.status = com.altis.library.loans.models.enums.LoanStatus.BORROWED then 1
                    when loan.status = com.altis.library.loans.models.enums.LoanStatus.RETURNED then 2
                    else 3
                end,
                loan.borrowedAt desc
            """)
    Page<LoanEntity> findActiveLoans(@Param("search") String search, Pageable pageable);

    @Query("""
            select loan from LoanEntity loan
            where loan.status = com.altis.library.loans.models.enums.LoanStatus.RETURNED
              and (
                    :search = ''
                 or lower(loan.book.title) like lower(concat('%', :search, '%'))
                 or lower(loan.user.name) like lower(concat('%', :search, '%'))
              )
            order by loan.returnedAt desc
            """)
    Page<LoanEntity> findReturnedLoans(@Param("search") String search, Pageable pageable);

    @Modifying
    @Query("""
            update LoanEntity loan
            set loan.status = com.altis.library.loans.models.enums.LoanStatus.OVERDUE
            where loan.status = com.altis.library.loans.models.enums.LoanStatus.BORROWED
              and loan.dueDate < :now
            """)
    int markOverdueLoans(@Param("now") LocalDateTime now);
}
