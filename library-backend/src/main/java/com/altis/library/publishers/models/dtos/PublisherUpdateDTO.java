package com.altis.library.publishers.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record PublisherUpdateDTO (
        @Schema(example = "John Doe")
        String name,

        @Schema(example = "JohnDoe@gmail.com")
        @Email(message = "Informe um E-Mail válido.")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$", message = "O E-mail deve ter terminação @gmail.com")
        String email,

        @Schema(example = "85912345678")
        @Pattern(regexp = "\\d+", message = "O telefone deve conter apenas numeros.")
        String phone,

        @Schema(example = "https://www.editora.com")
        @Pattern(regexp = "^(https?://(www\\.)?)?[a-zA-Z0-9]([a-zA-Z0-9-]*[a-zA-Z0-9])?(\\.[a-zA-Z0-9]([a-zA-Z0-9-]*[a-zA-Z0-9])?)*\\.[a-zA-Z]{2,}(:\\d{1,5})?(/\\S*)?$", message = "A URL do seu site não é valida!")
        String site
){}
