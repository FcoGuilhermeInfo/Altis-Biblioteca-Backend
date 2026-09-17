package com.altis.library.books.services;

import com.altis.library.books.models.dtos.BookRequestDTO;
import com.altis.library.books.models.dtos.BookResponseDTO;
import com.altis.library.books.models.dtos.BookUpdateDTO;
import com.altis.library.books.models.entities.Book;
import com.altis.library.books.repositories.BookRepository;
import com.altis.library.publishers.models.entities.Publisher;
import com.altis.library.publishers.repositories.PublisherRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;

    public BookService(BookRepository bookRepository, PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
    }

    //CREATE
    @Transactional
    public BookResponseDTO create(BookRequestDTO dto){
        Book book = new Book();

        Publisher publisher = publisherRepository.findByNameIgnoreCase(dto.publisherName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Editora não encontrada"));

        if (bookRepository.existsByTitleAndAuthorAndReleaseYearAndPublisher_Id(
                dto.title(), dto.author(), dto.releaseYear(), publisher.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este livro já está cadastrado com esses mesmos dados.");
        }

        book.setTitle(dto.title());
        book.setAuthor(dto.author());
        book.setReleaseYear(dto.releaseYear());
        book.setPublisher(publisher);
        book.setTotalQuantity(dto.totalQuantity());

        LocalDateTime now = LocalDateTime.now();
        book.setCreatedAt(now);
        book.setUpdatedAt(now);

        book.setBorrowedQuantity(0);

        Book saved = bookRepository.save(book);

        return toResponseDTO(saved);

    }

    // READ
    @Transactional
    public List<BookResponseDTO> findAll(){
        return bookRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // READ BY ID
    @Transactional
    public BookResponseDTO findById(UUID id){
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        return toResponseDTO(book);
    }

    // UPDATE
    @Transactional
    public BookResponseDTO update(UUID id, BookUpdateDTO dto){
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        if (dto.title() != null){
            book.setTitle(dto.title());
        }
        if (dto.author() != null){
            book.setAuthor(dto.author());
        }
        if (dto.releaseYear() != null){
            book.setAuthor(dto.author());
        }
        if (dto.publisherName() != null) {
            Publisher publisher = publisherRepository.findByNameIgnoreCase(dto.publisherName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Editora não encontrada"));

            book.setPublisher(publisher);
        }
        if (dto.totalQuantity() != null) {
            if (dto.totalQuantity() <= 0) {
                throw new RuntimeException("A quantidade total deve ser maior que zero.");
            }
            book.setTotalQuantity(dto.totalQuantity());
        }
        Book updated = bookRepository.save(book);
        return toResponseDTO(updated);
    }

    // DELETE - EM BREVE

    private BookResponseDTO toResponseDTO(Book book) {
        return new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getReleaseYear(),
                book.getPublisher().getName(),
                book.getAvailableQuantity()
        );
    }
}
