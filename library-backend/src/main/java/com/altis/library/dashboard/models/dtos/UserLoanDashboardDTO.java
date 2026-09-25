package com.altis.library.dashboard.models.dtos;

import com.altis.library.loans.models.enums.LoanStatus;

import java.time.LocalDateTime;

public record UserLoanDashboardDTO(
        String tenant,
        String book,
        LocalDateTime borrowedAt,
        LocalDateTime dueDate,
        LocalDateTime returnedAt,
        LoanStatus status
) {
}
