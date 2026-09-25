package com.altis.library.dashboard.models.dtos;

import java.util.List;

public record AdminDashboardResponseDTO(
        long totalBooks,
        long totalPublishers,
        long monthlyLoans,
        long totalUsers,
        LastLoanDashboardDTO lastLoan,
        MostBorrowedBookDTO mostBorrowedBook,
        List<MostBorrowedBookDTO> topBorrowedBooks
) {
}
