package com.altis.library.books.controllers;

import com.altis.library.books.models.dtos.BookRequestDTO;
import com.altis.library.books.models.dtos.BookResponseDTO;
import com.altis.library.books.models.dtos.BookUpdateDTO;
import com.altis.library.books.services.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public BookResponseDTO create(@Valid @RequestBody BookRequestDTO dto){
        return bookService.create(dto);
    }

    @GetMapping
    public Page<BookResponseDTO> findAll(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size){
        return bookService.findAll(search, PageRequest.of(page - 1, size));
    }

    @GetMapping("/{id}")
    public  BookResponseDTO findById(@PathVariable UUID id){
        return bookService.findById(id);
    }

    @PatchMapping("/{id}")
    public BookResponseDTO update(@PathVariable UUID id, @Valid @RequestBody BookUpdateDTO dto){
        return bookService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        bookService.delete(id);
    }

}
