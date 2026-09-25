package com.altis.library.dashboard.models.dtos;

import java.time.LocalDateTime;

public record LastLoanDashboardDTO(
        String bookTitle,
        String tenant,
        LocalDateTime borrowedAt,
        LocalDateTime dueDate
) {
}
