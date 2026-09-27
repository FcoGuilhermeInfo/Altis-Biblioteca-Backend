package com.altis.library.services;

import com.altis.library.books.mappers.BookMapper;
import com.altis.library.books.models.dtos.BookUpdateDTO;
import com.altis.library.books.models.entities.BookEntity;
import com.altis.library.books.repositories.BookRepository;
import com.altis.library.books.services.BookService;
import com.altis.library.loans.mappers.LoanMapper;
import com.altis.library.loans.models.entities.LoanEntity;
import com.altis.library.loans.repositories.LoanRepository;
import com.altis.library.loans.services.LoanService;
import com.altis.library.publishers.services.PublisherService;
import com.altis.library.shared.exception.BusinessExceptionException;
import com.altis.library.users.services.UserService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BusinessValidationTests {

    @Test
    void bookQuantityCannotBeReducedBelowBorrowedQuantity() {
        BookRepository bookRepository = mock(BookRepository.class);
        BookEntity book = new BookEntity();
        book.setId(UUID.randomUUID());
        book.setTotalQuantity(5);
        book.setBorrowedQuantity(3);
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        BookService service = new BookService(
                bookRepository, mock(LoanRepository.class), mock(PublisherService.class), mock(BookMapper.class));

        assertThrows(BusinessExceptionException.class, () -> service.update(
                book.getId(), new BookUpdateDTO(null, null, null, null, 2)));
        assertEquals(5, book.getTotalQuantity());
        verify(bookRepository, never()).save(book);
    }

    @Test
    void loanReturnTimestampIsSetByTheService() {
        LoanRepository loanRepository = mock(LoanRepository.class);
        BookService bookService = mock(BookService.class);
        BookEntity book = new BookEntity();
        book.setId(UUID.randomUUID());
        book.setBorrowedQuantity(1);
        LoanEntity loan = new LoanEntity();
        loan.setId(UUID.randomUUID());
        loan.setBook(book);
        loan.setBorrowedAt(LocalDateTime.now().minusDays(1));
        when(loanRepository.findById(loan.getId())).thenReturn(Optional.of(loan));
        when(bookService.findEntityById(book.getId())).thenReturn(book);

        LoanService service = new LoanService(
                loanRepository, bookService,
                mock(UserService.class), mock(LoanMapper.class));

        LocalDateTime beforeReturn = LocalDateTime.now();
        service.update(loan.getId());
        LocalDateTime afterReturn = LocalDateTime.now();

        assertTrue(!loan.getReturnedAt().isBefore(beforeReturn)
                && !loan.getReturnedAt().isAfter(afterReturn));
        verify(loanRepository).save(loan);
    }
}
