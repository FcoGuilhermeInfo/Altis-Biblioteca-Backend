package com.altis.library.users.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;



public record UserUpdateDTO (
    @Schema(example = "John Doe")
    String name,

    @Pattern(regexp = "\\d+", message = "O telefone deve conter apenas numeros.")
    @Schema(example = "85912345678")
    String phone,

    @Schema(example = "Av Dom Luis, Aldeota, 000")
    String address,

    @Email(message = "Informe um E-Mail válido.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$", message = "O E-mail deve ter terminação @gmail.com")
    @Schema(example = "JohnDoe@gmail.com")
    String email

    ){}
