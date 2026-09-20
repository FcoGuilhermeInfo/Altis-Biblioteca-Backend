package com.altis.library.books.services;

import com.altis.library.books.models.dtos.BookRequestDTO;
import com.altis.library.books.models.dtos.BookResponseDTO;
import com.altis.library.books.models.dtos.BookUpdateDTO;
import com.altis.library.books.mappers.BookMapper;
import com.altis.library.books.models.entities.BookEntity;
import com.altis.library.books.repositories.BookRepository;
import com.altis.library.loans.repositories.LoanRepository;
import com.altis.library.publishers.models.entities.Publisher;
import com.altis.library.publishers.services.PublisherService;
import com.altis.library.shared.exception.ConflictException;
import com.altis.library.shared.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final PublisherService publisherService;
    private final BookMapper mapper;

    public BookService(BookRepository bookRepository, LoanRepository loanRepository,
                       PublisherService publisherService, BookMapper mapper) {
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
        this.publisherService = publisherService;
        this.mapper = mapper;
    }

    //CREATE
    @Transactional
    public BookResponseDTO create(BookRequestDTO dto){
        Publisher publisher = publisherService.findByName(dto.publisherName());

        if (bookRepository.existsByTitleAndAuthorAndReleaseYearAndPublisher_Id(
                dto.title(), dto.author(), dto.releaseYear(), publisher.getId())) {
            throw new ConflictException("Este livro já está cadastrado com esses mesmos dados.");
        }

        BookEntity bookEntity = mapper.toEntity(dto, publisher);

        BookEntity saved = bookRepository.save(bookEntity);

        return mapper.toResponse(saved);

    }

    // READ
    public List<BookResponseDTO> findAll(){
        return mapper.toResponseList(bookRepository.findAll());
    }

    // READ BY ID
    public BookResponseDTO findById(UUID id){
        BookEntity bookEntity = findEntityById(id);

        return mapper.toResponse(bookEntity);
    }

    public BookEntity findEntityById(UUID id) {
        return bookRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));
    }

    public BookEntity save(BookEntity book) {
        return bookRepository.save(book);
    }

    // UPDATE
    @Transactional
    public BookResponseDTO update(UUID id, BookUpdateDTO dto){
        BookEntity bookEntity = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));

        if (dto.totalQuantity() != null && dto.totalQuantity() <= 0) {
            throw new RuntimeException("A quantidade total deve ser maior que zero.");
        }
        if (dto.publisherName() != null) {
            Publisher publisher = publisherService.findByName(dto.publisherName());

            bookEntity.setPublisher(publisher);
        }
        mapper.applyUpdate(dto, bookEntity);

        BookEntity updated = bookRepository.save(bookEntity);
        return mapper.toResponse(updated);
    }

    // DELETE
    @Transactional
    public void delete(UUID id) {
        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));

        if (loanRepository.existsByBookId(id)) {
            throw new ConflictException("Não é possível excluir um livro que possui empréstimos relacionados.");
        }

        bookRepository.delete(book);
    }
}
