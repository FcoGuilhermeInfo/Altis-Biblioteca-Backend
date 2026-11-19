
package com.altis.library.users.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserChangePasswordDTO(
        @NotBlank(message = "A senha atual é obrigatória.")
        @Schema(example = "senhaatual123")
        String currentPassword,

        @NotBlank(message = "A nova senha é obrigatória.")
        @Size(
                min = 8,
                max = 15,
                message = "A nova senha deve ter entre 8 e 15 caracteres."
        )
        @Schema(example = "novasenha123")
        String newPassword,

        @NotBlank(message = "A confirmação da senha é obrigatória.")
        @Schema(example = "novasenha123")
        String confirmPassword
) {
}
