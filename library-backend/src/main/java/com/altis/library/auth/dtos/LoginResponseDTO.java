package com.altis.library.auth.dtos;

public record LoginResponseDTO(
        String token,
        String type,
        long expiresIn
) {
}
