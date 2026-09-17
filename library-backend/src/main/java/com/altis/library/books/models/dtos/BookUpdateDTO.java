package com.altis.library.books.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;


public record BookUpdateDTO(


        @Schema(example = "Harry Potter")
        String title,

        @Schema(example = "J K Rollings")
        String author,

        @Min(value = 1, message = "O ano deve ser maior que zero")
        @Max(value = Short.MAX_VALUE, message = "O ano informado é inválido")
        @Schema(example = "2000")
        Short releaseYear,

        @Schema(example = "Editora Rocco")
        String publisherName,

        @Min(value = 1, message = "A quantidade de livros deve ser maior que zero")
        @Schema(example = "50")
        Integer totalQuantity


) {
}
