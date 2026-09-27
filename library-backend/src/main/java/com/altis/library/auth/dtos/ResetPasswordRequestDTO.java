package com.altis.library.auth.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequestDTO(
        @NotBlank(message = "O token é obrigatório.")
        @Size(max = 128, message = "O token informado é inválido.")
        @Schema(example = "token-recebido-no-primeiro-endpoint")
        String token,

        @NotBlank(message = "A nova senha é obrigatória.")
        @Size(min = 8, max = 15, message = "A senha deve ter entre 8 e 15 caracteres.")
        @Schema(example = "novasenha123")
        String newPassword,

        @NotBlank(message = "A confirmação da senha é obrigatória.")
        @Size(min = 8, max = 15, message = "A senha deve ter entre 8 e 15 caracteres.")
        @Schema(example = "novasenha123")
        String confirmPassword
) {
}
