package com.altis.library.books.services;

import com.altis.library.books.models.dtos.BookRequestDTO;
import com.altis.library.books.models.dtos.BookResponseDTO;
import com.altis.library.books.models.dtos.BookUpdateDTO;
import com.altis.library.books.models.entities.BookEntity;
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
        BookEntity bookEntity = new BookEntity();

        Publisher publisher = publisherRepository.findByNameIgnoreCase(dto.publisherName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Editora não encontrada"));

        if (bookRepository.existsByTitleAndAuthorAndReleaseYearAndPublisher_Id(
                dto.title(), dto.author(), dto.releaseYear(), publisher.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este livro já está cadastrado com esses mesmos dados.");
        }

        bookEntity.setTitle(dto.title());
        bookEntity.setAuthor(dto.author());
        bookEntity.setReleaseYear(dto.releaseYear());
        bookEntity.setPublisher(publisher);
        bookEntity.setTotalQuantity(dto.totalQuantity());

        LocalDateTime now = LocalDateTime.now();
        bookEntity.setCreatedAt(now);
        bookEntity.setUpdatedAt(now);

        bookEntity.setBorrowedQuantity(0);

        BookEntity saved = bookRepository.save(bookEntity);

        return toResponseDTO(saved);

    }

    // READ
    public List<BookResponseDTO> findAll(){
        return bookRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // READ BY ID
    public BookResponseDTO findById(UUID id){
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        return toResponseDTO(bookEntity);
    }

    // UPDATE
    @Transactional
    public BookResponseDTO update(UUID id, BookUpdateDTO dto){
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        if (dto.title() != null){
            bookEntity.setTitle(dto.title());
        }
        if (dto.author() != null){
            bookEntity.setAuthor(dto.author());
        }
        if (dto.releaseYear() != null){
            bookEntity.setAuthor(dto.author());
        }
        if (dto.publisherName() != null) {
            Publisher publisher = publisherRepository.findByNameIgnoreCase(dto.publisherName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Editora não encontrada"));

            bookEntity.setPublisher(publisher);
        }
        if (dto.totalQuantity() != null) {
            if (dto.totalQuantity() <= 0) {
                throw new RuntimeException("A quantidade total deve ser maior que zero.");
            }
            bookEntity.setTotalQuantity(dto.totalQuantity());
        }
        BookEntity updated = bookRepository.save(bookEntity);
        return toResponseDTO(updated);
    }

    // DELETE - EM BREVE

    private BookResponseDTO toResponseDTO(BookEntity bookEntity) {
        return new BookResponseDTO(
                bookEntity.getId(),
                bookEntity.getTitle(),
                bookEntity.getAuthor(),
                bookEntity.getReleaseYear(),
                bookEntity.getPublisher().getName(),
                bookEntity.getAvailableQuantity()
        );
    }
}
