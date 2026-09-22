package com.altis.library.publishers.mappers;

import com.altis.library.publishers.models.dtos.PublisherRequestDTO;
import com.altis.library.publishers.models.dtos.PublisherResponseDTO;
import com.altis.library.publishers.models.entities.PublisherEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PublisherMapper {

    public PublisherResponseDTO toResponse(PublisherEntity entity) {
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

    public List<PublisherResponseDTO> toResponseList(List<PublisherEntity> entities) {
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }

    public PublisherEntity toEntity(PublisherRequestDTO dto) {
        LocalDateTime now = LocalDateTime.now();

        PublisherEntity entity = new PublisherEntity();
        entity.setName(dto.name());
        entity.setEmail(dto.email());
        entity.setPhone(dto.phone());
        entity.setSite(dto.site());
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        return entity;
    }

    public void applyUpdate(PublisherRequestDTO dto, PublisherEntity entity) {
        entity.setName(dto.name());
        entity.setEmail(dto.email());
        entity.setPhone(dto.phone());
        entity.setSite(dto.site());
        entity.setUpdatedAt(LocalDateTime.now());
    }
}
