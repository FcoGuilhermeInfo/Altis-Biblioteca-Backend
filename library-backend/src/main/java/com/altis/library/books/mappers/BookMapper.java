package com.altis.library.books.mappers;

import com.altis.library.books.models.dtos.BookRequestDTO;
import com.altis.library.books.models.dtos.BookResponseDTO;
import com.altis.library.books.models.dtos.BookUpdateDTO;
import com.altis.library.books.models.entities.BookEntity;
import com.altis.library.publishers.models.entities.Publisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class BookMapper {

    public BookResponseDTO toResponse(BookEntity entity) {
        return new BookResponseDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getAuthor(),
                entity.getReleaseYear(),
                entity.getPublisher().getName(),
                entity.getTotalQuantity() - entity.getBorrowedQuantity()
        );
    }

    public List<BookResponseDTO> toResponseList(List<BookEntity> entities) {
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }

    public BookEntity toEntity(BookRequestDTO dto, Publisher publisher) {
        LocalDateTime now = LocalDateTime.now();

        BookEntity entity = new BookEntity();
        entity.setTitle(dto.title());
        entity.setAuthor(dto.author());
        entity.setReleaseYear(dto.releaseYear());
        entity.setPublisher(publisher);
        entity.setTotalQuantity(dto.totalQuantity());
        entity.setBorrowedQuantity(0);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        return entity;
    }

    public void applyUpdate(BookUpdateDTO dto, BookEntity entity) {
        applyUpdate(dto, entity, null);
    }

    public void applyUpdate(BookUpdateDTO dto, BookEntity entity, Publisher publisher) {
        if (dto.title() != null) {
            entity.setTitle(dto.title());
        }
        if (dto.author() != null) {
            entity.setAuthor(dto.author());
        }
        if (dto.releaseYear() != null) {
            entity.setReleaseYear(dto.releaseYear());
        }
        if (dto.publisherName() != null && publisher != null) {
            entity.setPublisher(publisher);
        }
        if (dto.totalQuantity() != null) {
            entity.setTotalQuantity(dto.totalQuantity());
        }

        entity.setUpdatedAt(LocalDateTime.now());
    }
}
