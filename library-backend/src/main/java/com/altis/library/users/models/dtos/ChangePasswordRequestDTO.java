package com.altis.library.users.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record ChangePasswordRequestDTO(
        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "Informe um e-mail válido.")
        @Schema(example = "JohnDoe@gmail.com")
        String email,

        @NotBlank(message = "O CPF é obrigatório.")
        @Pattern(regexp = "\\d{11}", message = "Envie um CPF válido.")
        @CPF(message = "O CPF inserido não é válido!")
        @Schema(example = "12345678900")
        String cpf,

        @NotBlank(message = "A nova senha é obrigatória.")
        @Size(min = 8, max = 15, message = "A senha deve ter entre 8 e 15 caracteres")
        @Schema(example = "novasenha123")
        String newPassword
) {
}
