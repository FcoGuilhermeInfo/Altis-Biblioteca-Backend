package com.altis.library.auth.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

public record ForgotPasswordRequestDTO(
        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "Informe um e-mail válido.")
        @Schema(example = "usuario@gmail.com")
        String email,

        @NotBlank(message = "O CPF é obrigatório.")
        @Pattern(regexp = "\\d{11}", message = "Envie um CPF válido.")
        @CPF(message = "O CPF inserido não é válido!")
        @Schema(example = "12345678900")
        String cpf
) {
}
