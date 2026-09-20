package com.altis.library.loans.models.dtos;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record LoanUpdateDTO(
        @NotNull(message = "A data de devolução é obrigatória.")
        LocalDateTime returnedAt
) {
}
