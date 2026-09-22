package com.altis.library.loans.models.dtos;

import com.altis.library.loans.models.enums.LoanStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record LoanResponseDTO(
        UUID id,
        String bookTitle,
        String userFullName,
        LocalDateTime borrowedAt,
        LocalDateTime dueDate,
        LocalDateTime returnedAt,
        LoanStatus status
) {
}
