package com.altis.library.dashboard.models.dtos;

import java.util.UUID;

public record MostBorrowedBookDTO(
        UUID bookId,
        String bookTitle,
        Long loanCount
) {
}
