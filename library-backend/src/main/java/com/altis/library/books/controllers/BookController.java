package com.altis.library.books.controllers;

import com.altis.library.books.models.dtos.BookRequestDTO;
import com.altis.library.books.models.dtos.BookResponseDTO;
import com.altis.library.books.models.dtos.BookUpdateDTO;
import com.altis.library.books.services.BookService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    public List<BookResponseDTO> findAll(){
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public  BookResponseDTO findById(@PathVariable UUID id){
        return bookService.findById(id);
    }

    @PatchMapping("/{id}")
    public BookResponseDTO update(@PathVariable UUID id, @Valid @RequestBody BookUpdateDTO dto){
        return bookService.update(id, dto);
    }

}
