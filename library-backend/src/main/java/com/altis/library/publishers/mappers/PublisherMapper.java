package com.altis.library.publishers.mappers;

import com.altis.library.publishers.models.dtos.PublisherRequestDTO;
import com.altis.library.publishers.models.dtos.PublisherResponseDTO;
import com.altis.library.publishers.models.entities.Publisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PublisherMapper {

    public PublisherResponseDTO toResponse(Publisher entity) {
        return new PublisherResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getSite(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public List<PublisherResponseDTO> toResponseList(List<Publisher> entities) {
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }

    public Publisher toEntity(PublisherRequestDTO dto) {
        LocalDateTime now = LocalDateTime.now();

        Publisher entity = new Publisher();
        entity.setName(dto.name());
        entity.setEmail(dto.email());
        entity.setPhone(dto.phone());
        entity.setSite(dto.site());
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        return entity;
    }

    public void applyUpdate(PublisherRequestDTO dto, Publisher entity) {
        entity.setName(dto.name());
        entity.setEmail(dto.email());
        entity.setPhone(dto.phone());
        entity.setSite(dto.site());
        entity.setUpdatedAt(LocalDateTime.now());
    }
}
