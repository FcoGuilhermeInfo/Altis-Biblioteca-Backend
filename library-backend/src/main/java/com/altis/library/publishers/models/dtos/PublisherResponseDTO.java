package com.altis.library.publishers.models.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record PublisherResponseDTO (
    UUID id,
    String name,
    String email,
    String phone,
    String site,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
){}
