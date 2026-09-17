package com.altis.library.books.models.dtos;

import java.util.UUID;

public record BookResponseDTO(
        UUID id,
        String name,
        String author,
        Short releaseYear,
        String publisherName,
        Integer availableQuantity
){}
