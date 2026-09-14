package com.altis.library.users.models.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record UserRequestDTO(

        @NotBlank(message = "O nome é obrigatório!")
        @Schema(example = "John Doe")
        String name,

        @NotNull(message = "A data de nascimento é obrigatória!")
        @Schema(example = "20/02/2002")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate birthDate,

        @NotBlank(message = "O CPF é obrigatório!")
        @Schema(example = "12345678900")
        @Pattern(regexp = "\\d{11}", message = "Envie um CPF válido.")
        @CPF(message = "O CPF inserido não é válido!")
        String cpf,

        @NotBlank(message = "O telefone é obrigatório!")
        @Schema(example = "85912345678")
        @Pattern(regexp = "\\d+", message = "O telefone deve conter apenas numeros.")
        String phone,

        @NotBlank(message = "O endereço é obrigatório!")
        @Schema(example = "Av Dom Luis, Aldeota, 000")
        String address,

        @NotBlank(message = "O E-mail é obrigatório!")
        @Schema(example = "JohnDoe@gmail.com")
        @Email(message = "Informe um E-Mail válido.")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$", message = "O E-mail deve ter terminação @gmail.com")
        String email,

        @NotBlank(message = "Sua senha é obrigatória!")
        @Schema(example = "senhaforte123")
        @Size(min = 8, max = 15, message = "A senha deve ter entre 8 e 15 caracteres")
        String password

) {}