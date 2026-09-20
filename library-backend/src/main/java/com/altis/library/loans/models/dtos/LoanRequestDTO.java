package com.altis.library.loans.models.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record LoanRequestDTO(
        @NotNull(message = "O livro é obrigatório.")
        UUID bookId,
        @NotNull(message = "O usuário é obrigatório.")
        UUID userId
) {
}
