package com.altis.library.users.mappers;

import com.altis.library.users.models.dtos.UserRequestDTO;
import com.altis.library.users.models.dtos.UserResponseDTO;
import com.altis.library.users.models.dtos.UserUpdateDTO;
import com.altis.library.users.models.entities.UserEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class UserMapper {

    public UserResponseDTO toResponse(UserEntity entity) {
        return new UserResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getBirthDate(),
                entity.getCpf(),
                entity.getPhone(),
                entity.getAddress(),
                entity.getEmail(),
                entity.isAdmin(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public List<UserResponseDTO> toResponseList(List<UserEntity> entities) {
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }

    public UserEntity toEntity(UserRequestDTO dto) {
        LocalDateTime now = LocalDateTime.now();

        UserEntity entity = new UserEntity();
        entity.setName(dto.name());
        entity.setBirthDate(dto.birthDate());
        entity.setCpf(dto.cpf());
        entity.setPhone(dto.phone());
        entity.setAddress(dto.address());
        entity.setEmail(dto.email());
        entity.setAdmin(false);
        entity.setActive(true);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        return entity;
    }

    public void applyUpdate(UserUpdateDTO dto, UserEntity entity) {
        if (dto.name() != null) {
            entity.setName(dto.name());
        }
        if (dto.phone() != null) {
            entity.setPhone(dto.phone());
        }
        if (dto.address() != null) {
            entity.setAddress(dto.address());
        }
        if (dto.email() != null) {
            entity.setEmail(dto.email());
        }

        entity.setUpdatedAt(LocalDateTime.now());
    }
}
