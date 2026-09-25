package com.altis.library.loans.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record LoanRequestDTO(
        @NotNull(message = "O livro é obrigatório.")
        UUID bookId,

        @NotNull(message = "O usuário é obrigatório.")
        UUID userId,

        @NotNull(message = "A data prevista de devolução é obrigatória.")
        @Future(message = "A data prevista de devolução deve ser futura.")
        @Schema(example = "2026-10-06T23:59:59")
        LocalDateTime dueDate
) {
}
