package com.altis.library.dashboard.models.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record UserDashboardResponseDTO(
        long borrowedBooks,
        int borrowingLimit,
        LocalDateTime nextReturnDate,
        String lastBorrowedBook,
        LocalDateTime lastBorrowedAt,
        List<UserLoanDashboardDTO> loans
) {
}
