package com.altis.library.users.services;

import com.altis.library.users.models.dtos.UserRequestDTO;
import com.altis.library.users.models.dtos.UserResponseDTO;
import com.altis.library.users.models.dtos.UserUpdateDTO;
import com.altis.library.users.models.entities.User;
import com.altis.library.users.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // CREATE
    public UserResponseDTO create(UserRequestDTO dto) {

        User user = new User();

        user.setName(dto.name());
        user.setBirthDate(dto.birthDate());
        user.setCpf(dto.cpf());
        user.setPhone(dto.phone());
        user.setAddress(dto.address());
        user.setEmail(dto.email());
        user.setPassword(dto.password());

        user.setAdmin(false);
        user.setActive(true);

        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        userRepository.save(user);

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getBirthDate(),
                user.getCpf(),
                user.getPhone(),
                user.getAddress(),
                user.getEmail(),
                user.isAdmin(),
                user.isActive(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    // READ ALL
    public List<UserResponseDTO> findAll() {

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getName(),
                        user.getBirthDate(),
                        user.getCpf(),
                        user.getPhone(),
                        user.getAddress(),
                        user.getEmail(),
                        user.isAdmin(),
                        user.isActive(),
                        user.getCreatedAt(),
                        user.getUpdatedAt()
                ))
                .toList();
    }

    // READ BY ID
    public UserResponseDTO findById(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getBirthDate(),
                user.getCpf(),
                user.getPhone(),
                user.getAddress(),
                user.getEmail(),
                user.isAdmin(),
                user.isActive(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    // UPDATE
    public UserResponseDTO update(UUID id, UserUpdateDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        if (dto.name() != null) {
            user.setName(dto.name());
        }

        if (dto.phone() != null) {
            user.setPhone(dto.phone());
        }

        if (dto.address() != null) {
            user.setAddress(dto.address());
        }

        if (dto.email() != null) {
            user.setEmail(dto.email());
        }

        user.setUpdatedAt(LocalDateTime.now());

        User updated = userRepository.save(user);

        return new UserResponseDTO(
                updated.getId(),
                updated.getName(),
                updated.getBirthDate(),
                updated.getCpf(),
                updated.getPhone(),
                updated.getAddress(),
                updated.getEmail(),
                updated.isAdmin(),
                updated.isActive(),
                updated.getCreatedAt(),
                updated.getUpdatedAt()
        );
    }

    // DELETE
    public void delete(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        userRepository.delete(user);
    }
}