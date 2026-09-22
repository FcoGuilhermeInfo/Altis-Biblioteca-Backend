package com.altis.library.loans.mappers;

import com.altis.library.books.models.entities.BookEntity;
import com.altis.library.loans.models.dtos.LoanResponseDTO;
import com.altis.library.loans.models.entities.LoanEntity;
import com.altis.library.loans.models.enums.LoanStatus;
import com.altis.library.users.models.entities.UserEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class LoanMapper {

    public LoanResponseDTO toResponse(LoanEntity entity) {
        return new LoanResponseDTO(
                entity.getId(),
                entity.getBook().getTitle(),
                entity.getUser().getName(),
                entity.getBorrowedAt(),
                entity.getDueDate(),
                entity.getReturnedAt(),
                entity.getStatus()
        );
    }

    public List<LoanResponseDTO> toResponseList(List<LoanEntity> entities) {
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }

    public LoanEntity toEntity(LoanEntity entity, BookEntity book, UserEntity user,
                               LocalDateTime dueDate) {
        LocalDateTime now = LocalDateTime.now();
        entity.setBook(book);
        entity.setUser(user);
        entity.setBorrowedAt(now);
        entity.setDueDate(dueDate);
        entity.setStatus(LoanStatus.BORROWED);
        return entity;
    }
}
