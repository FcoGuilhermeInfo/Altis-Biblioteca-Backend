package com.altis.library.loans.services;

import com.altis.library.books.models.entities.BookEntity;
import com.altis.library.books.services.BookService;
import com.altis.library.loans.mappers.LoanMapper;
import com.altis.library.loans.models.dtos.LoanRequestDTO;
import com.altis.library.loans.models.dtos.LoanResponseDTO;
import com.altis.library.loans.models.dtos.LoanUpdateDTO;
import com.altis.library.loans.models.entities.LoanEntity;
import com.altis.library.loans.models.enums.LoanStatus;
import com.altis.library.loans.repositories.LoanRepository;
import com.altis.library.shared.exception.BusinessExceptionException;
import com.altis.library.shared.exception.ResourceNotFoundException;
import com.altis.library.users.models.entities.UserEntity;
import com.altis.library.users.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class LoanService {
    private static final int MAX_ACTIVE_LOANS = 5;

    private final LoanRepository loanRepository;
    private final BookService bookService;
    private final UserService userService;
    private final LoanMapper loanMapper;

    public LoanService(LoanRepository loanRepository, BookService bookService,
                       UserService userService, LoanMapper loanMapper) {
        this.loanRepository = loanRepository;
        this.bookService = bookService;
        this.userService = userService;
        this.loanMapper = loanMapper;
    }

    @Transactional
    public LoanResponseDTO create(LoanRequestDTO dto) {
        UserEntity user = userService.findEntityById(dto.userId());
        if (!user.isActive()) {
            throw new BusinessExceptionException("O usuário precisa estar ativo para realizar empréstimos.");
        }
        if (loanRepository.countByUserIdAndReturnedAtIsNull(user.getId()) >= MAX_ACTIVE_LOANS) {
            throw new BusinessExceptionException("O usuário atingiu o limite de 5 empréstimos ativos.");
        }

        BookEntity book = bookService.findEntityById(dto.bookId());
        if (book.getBorrowedQuantity() >= book.getTotalQuantity()) {
            throw new BusinessExceptionException("Não há exemplares disponíveis para este livro.");
        }

        book.setBorrowedQuantity(book.getBorrowedQuantity() + 1);
        book.setUpdatedAt(LocalDateTime.now());
        bookService.save(book);

        LoanEntity loan = loanMapper.toEntity(new LoanEntity(), book, user, dto.dueDate());
        return loanMapper.toResponse(loanRepository.save(loan));
    }

    @Transactional
    public LoanResponseDTO update(UUID id, LoanUpdateDTO dto) {
        LoanEntity loan = findEntity(id);
        if (loan.getReturnedAt() != null) {
            throw new BusinessExceptionException("Este empréstimo já foi finalizado.");
        }

        BookEntity book = bookService.findEntityById(loan.getBook().getId());
        if (book.getBorrowedQuantity() <= 0) {
            throw new BusinessExceptionException("A disponibilidade do livro está inconsistente.");
        }
        book.setBorrowedQuantity(book.getBorrowedQuantity() - 1);
        book.setUpdatedAt(LocalDateTime.now());
        bookService.save(book);

        loan.setReturnedAt(dto.returnedAt());
        loan.setStatus(LoanStatus.RETURNED);
        return loanMapper.toResponse(loanRepository.save(loan));
    }

    @Transactional
    public Page<LoanResponseDTO> findAll(String search, Pageable pageable) {
        loanRepository.markOverdueLoans(LocalDateTime.now());
        Page<LoanEntity> loans = loanRepository.findActiveLoans(normalizeSearch(search), pageable);
        return loans.map(loanMapper::toResponse);
    }

    @Transactional
    public Page<LoanResponseDTO> findHistory(String search, Pageable pageable) {
        Page<LoanEntity> loans = loanRepository.findReturnedLoans(normalizeSearch(search), pageable);
        return loans.map(loanMapper::toResponse);
    }

    public LoanResponseDTO findById(UUID id) {
        LoanEntity loan = findEntity(id);
        updateOverdueStatus(loan);
        return loanMapper.toResponse(loan);
    }

    private LoanEntity findEntity(UUID id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", id));
    }

    private String normalizeSearch(String search) {
        return search == null || search.isBlank() ? "" : search.trim();
    }

    private void updateOverdueStatus(LoanEntity loan) {
        if (loan.getStatus() == LoanStatus.BORROWED
                && loan.getDueDate().isBefore(LocalDateTime.now())) {
            loan.setStatus(LoanStatus.OVERDUE);
            loanRepository.save(loan);
        }
    }
}
