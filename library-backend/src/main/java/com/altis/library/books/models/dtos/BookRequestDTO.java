package com.altis.library.books.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record BookRequestDTO(

        @NotBlank(message = "O titulo é obrigatório!")
        @Schema(example = "Harry Potter")
        String title,

        @NotBlank(message = "O autor é obrigatório!")
        @Schema(example = "J K Rollings")
        String author,

        @NotNull(message = "O ano é obrigatório")
        @Min(value = 1, message = "O ano deve ser maior que zero")
        @Max(value = Short.MAX_VALUE, message = "O ano informado é inválido")
        @Schema(example = "2000")
        Short releaseYear,

        @NotBlank(message = "A editora é obrigatória")
        @Schema(example = "Editora Rocco")
        String publisherName,

        @NotNull(message = "A quantidade de livros é obrigatória!")
        @Min(value = 1, message = "A quantidade de livros deve ser maior que zero")
        @Schema(example = "50")
        Integer totalQuantity


) {
}
