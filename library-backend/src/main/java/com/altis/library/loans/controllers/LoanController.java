package com.altis.library.loans.controllers;

import com.altis.library.loans.models.dtos.LoanRequestDTO;
import com.altis.library.loans.models.dtos.LoanResponseDTO;
import com.altis.library.loans.models.dtos.LoanUpdateDTO;
import com.altis.library.loans.services.LoanService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/loans")
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public LoanResponseDTO create(@Valid @RequestBody LoanRequestDTO dto) {
        return loanService.create(dto);
    }

    @GetMapping
    public Page<LoanResponseDTO> findAll(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return loanService.findAll(search, PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public LoanResponseDTO findById(@PathVariable UUID id) {
        return loanService.findById(id);
    }

    @PatchMapping("/{id}")
    public LoanResponseDTO update(@PathVariable UUID id, @Valid @RequestBody LoanUpdateDTO dto) {
        return loanService.update(id, dto);
    }
}
