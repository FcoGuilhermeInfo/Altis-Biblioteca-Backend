package com.altis.library.users.models.dtos;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDTO(
    UUID id,
    String name,
    LocalDate birthDate,
    String cpf,
    String phone,
    String address,
    String email,
    Boolean admin,
    Boolean active,
    LocalDateTime createdAt,
    LocalDateTime updatedAt


) {}
