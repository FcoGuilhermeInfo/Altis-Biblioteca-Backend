package com.altis.library.dashboard.services;

import com.altis.library.books.repositories.BookRepository;
import com.altis.library.dashboard.mappers.DashboardMapper;
import com.altis.library.dashboard.models.dtos.AdminDashboardResponseDTO;
import com.altis.library.dashboard.models.dtos.MostBorrowedBookDTO;
import com.altis.library.dashboard.models.dtos.UserDashboardResponseDTO;
import com.altis.library.loans.models.entities.LoanEntity;
import com.altis.library.loans.repositories.LoanRepository;
import com.altis.library.publishers.repositories.PublisherRepository;
import com.altis.library.users.models.entities.UserEntity;
import com.altis.library.users.repositories.UserRepository;
import com.altis.library.users.services.UserService;
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
    private static final int TOP_BOOKS_LIMIT = 3;

    private final UserService userService;
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final DashboardMapper dashboardMapper;

    public DashboardService(UserService userService,
                            BookRepository bookRepository,
                            PublisherRepository publisherRepository,
                            LoanRepository loanRepository,
                            UserRepository userRepository,
                            DashboardMapper dashboardMapper) {
        this.userService = userService;
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.dashboardMapper = dashboardMapper;
    }

    @Transactional
    public UserDashboardResponseDTO getUserDashboard(Authentication authentication) {
        UserEntity user = userService.findByAuthentication(authentication);
        LocalDateTime now = LocalDateTime.now();
        loanRepository.markOverdueLoans(now);

        List<LoanEntity> loans = loanRepository.findByUserIdOrderByBorrowedAtDesc(user.getId());
        LoanEntity lastLoan = loans.isEmpty() ? null : loans.get(0);
        LocalDateTime nextReturnDate = findNextReturnDate(user, now);

        return new UserDashboardResponseDTO(
                loanRepository.countByUserIdAndReturnedAtIsNull(user.getId()),
                MAX_ACTIVE_LOANS,
                nextReturnDate,
                lastLoan == null ? null : lastLoan.getBook().getTitle(),
                lastLoan == null ? null : lastLoan.getBorrowedAt(),
                dashboardMapper.toUserLoanDTOs(loans)
        );
    }

    @Transactional(readOnly = true)
    public AdminDashboardResponseDTO getAdminDashboard() {
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime start = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime end = currentMonth.plusMonths(1).atDay(1).atStartOfDay();

        List<MostBorrowedBookDTO> topBooks = findTopBorrowedBooks(start, end);
        LoanEntity lastLoan = loanRepository.findFirstByOrderByBorrowedAtDesc().orElse(null);

        return new AdminDashboardResponseDTO(
                bookRepository.count(),
                publisherRepository.count(),
                loanRepository.countByBorrowedAtGreaterThanEqualAndBorrowedAtLessThan(start, end),
                userRepository.count(),
                dashboardMapper.toLastLoanDTO(lastLoan),
                topBooks.isEmpty() ? null : topBooks.get(0),
                topBooks
        );
    }

    private LocalDateTime findNextReturnDate(UserEntity user, LocalDateTime now) {
        return loanRepository
                .findFirstByUserIdAndReturnedAtIsNullAndDueDateGreaterThanEqualOrderByDueDateAsc(
                        user.getId(), now)
                .map(LoanEntity::getDueDate)
                .orElse(null);
    }

    private List<MostBorrowedBookDTO> findTopBorrowedBooks(LocalDateTime start,
                                                            LocalDateTime end) {
        return loanRepository.findMostBorrowedBooksBetween(
                start, end, PageRequest.of(0, TOP_BOOKS_LIMIT));
    }
}
