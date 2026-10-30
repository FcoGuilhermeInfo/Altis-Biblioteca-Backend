
package com.altis.library.books.models.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record BookResponseDTO(
    UUID id,
    String title,
    String author,
    Short releaseYear,
    String publisherName,
    Integer totalQuantity,
    Integer availableQuantity,
    Integer borrowedQuantity,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
