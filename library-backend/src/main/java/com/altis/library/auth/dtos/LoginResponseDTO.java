package com.altis.library.auth.dtos;

import java.util.UUID;

public record LoginResponseDTO(
        UUID user_id,
        String token,
        String type,
        long expiresIn
) {
}
