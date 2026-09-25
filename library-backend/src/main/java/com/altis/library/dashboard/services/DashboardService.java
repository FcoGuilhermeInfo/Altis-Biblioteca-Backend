package com.altis.library.dashboard.services;

import com.altis.library.books.repositories.BookRepository;
import com.altis.library.dashboard.models.dtos.AdminDashboardResponseDTO;
import com.altis.library.dashboard.models.dtos.LastLoanDashboardDTO;
import com.altis.library.dashboard.models.dtos.MostBorrowedBookDTO;
import com.altis.library.dashboard.models.dtos.UserDashboardResponseDTO;
import com.altis.library.dashboard.models.dtos.UserLoanDashboardDTO;
import com.altis.library.loans.models.entities.LoanEntity;
import com.altis.library.loans.repositories.LoanRepository;
import com.altis.library.publishers.repositories.PublisherRepository;
import com.altis.library.shared.exception.ResourceNotFoundException;
import com.altis.library.users.models.entities.UserEntity;
import com.altis.library.users.repositories.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
public class DashboardService {
    private static final int MAX_ACTIVE_LOANS = 5;

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final LoanRepository loanRepository;

    public DashboardService(UserRepository userRepository,
                            BookRepository bookRepository,
                            PublisherRepository publisherRepository,
                            LoanRepository loanRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
        this.loanRepository = loanRepository;
    }

    @Transactional
    public UserDashboardResponseDTO getUserDashboard(Authentication authentication) {
        UserEntity user = findAuthenticatedUser(authentication);
        LocalDateTime now = LocalDateTime.now();
        loanRepository.markOverdueLoans(now);
        List<LoanEntity> loans = loanRepository.findByUserIdOrderByBorrowedAtDesc(user.getId());
        LoanEntity lastLoan = loans.isEmpty() ? null : loans.get(0);
        LocalDateTime nextReturnDate = loanRepository
                .findFirstByUserIdAndReturnedAtIsNullAndDueDateGreaterThanEqualOrderByDueDateAsc(
                        user.getId(), now)
                .map(LoanEntity::getDueDate)
                .orElse(null);

        List<UserLoanDashboardDTO> loanDTOs = loans.stream()
                .map(loan -> new UserLoanDashboardDTO(
                        loan.getUser().getName(),
                        loan.getBook().getTitle(),
                        loan.getBorrowedAt(),
                        loan.getDueDate(),
                        loan.getReturnedAt(),
                        loan.getStatus()
                ))
                .toList();

        return new UserDashboardResponseDTO(
                loanRepository.countByUserIdAndReturnedAtIsNull(user.getId()),
                MAX_ACTIVE_LOANS,
                nextReturnDate,
                lastLoan == null ? null : lastLoan.getBook().getTitle(),
                lastLoan == null ? null : lastLoan.getBorrowedAt(),
                loanDTOs
        );
    }

    @Transactional(readOnly = true)
    public AdminDashboardResponseDTO getAdminDashboard() {
        LocalDateTime start = YearMonth.now().atDay(1).atStartOfDay();
        LocalDateTime end = YearMonth.now().plusMonths(1).atDay(1).atStartOfDay();
        List<MostBorrowedBookDTO> topBooks = loanRepository.findMostBorrowedBooksBetween(
                start, end, PageRequest.of(0, 3));
        LoanEntity lastLoan = loanRepository.findFirstByOrderByBorrowedAtDesc().orElse(null);

        return new AdminDashboardResponseDTO(
                bookRepository.count(),
                publisherRepository.count(),
                loanRepository.countByBorrowedAtGreaterThanEqualAndBorrowedAtLessThan(start, end),
                userRepository.count(),
                toLastLoanDTO(lastLoan),
                topBooks.isEmpty() ? null : topBooks.get(0),
                topBooks
        );
    }

    private UserEntity findAuthenticatedUser(Authentication authentication) {
        if (authentication == null || authentication.getName() == null
                || authentication.getName().isBlank()) {
            throw new ResourceNotFoundException("User", "authenticated principal");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User", authentication.getName()));
    }

    private LastLoanDashboardDTO toLastLoanDTO(LoanEntity loan) {
        if (loan == null) {
            return null;
        }
        return new LastLoanDashboardDTO(
                loan.getBook().getTitle(),
                loan.getUser().getName(),
                loan.getBorrowedAt(),
                loan.getDueDate()
        );
    }
}
