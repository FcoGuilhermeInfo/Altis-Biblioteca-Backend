package com.altis.library.users.services;

import com.altis.library.users.models.dtos.UserRequestDTO;
import com.altis.library.users.models.dtos.UserResponseDTO;
import com.altis.library.users.models.dtos.UserUpdateDTO;
import com.altis.library.users.models.dtos.ChangePasswordRequestDTO;
import com.altis.library.users.models.entities.UserEntity;
import com.altis.library.users.mappers.UserMapper;
import com.altis.library.users.repositories.UserRepository;
import com.altis.library.shared.exception.ResourceNotFoundException;
import com.altis.library.shared.exception.UnauthorizedException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // CREATE
    @Transactional
    public UserResponseDTO create(UserRequestDTO dto) {
        var user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.password()));
        var savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    // READ ALL
    public Page<UserResponseDTO> findAll(String search, Pageable pageable) {
        Page<UserEntity> users = search == null || search.isBlank()
                ? userRepository.findByAdminFalse(pageable)
                : userRepository.findByAdminFalseAndNameContainingIgnoreCase(search.trim(), pageable);
        return users.map(userMapper::toResponse);
    }

    // READ BY ID
    public UserResponseDTO findById(UUID id) {
        var user = findEntityById(id);

        return userMapper.toResponse(user);
    }

    public UserEntity findEntityById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    public UserEntity findByAuthentication(Authentication authentication) {
        if (authentication == null || authentication.getName() == null
                || authentication.getName().isBlank()) {
            throw new ResourceNotFoundException("User", "authenticated principal");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User", authentication.getName()));
    }

    // UPDATE
    @Transactional
    public UserResponseDTO update(UUID id, UserUpdateDTO dto) {

        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        ensureNotAdmin(user);

        userMapper.applyUpdate(dto, user);

        var updated = userRepository.save(user);
        return userMapper.toResponse(updated);
    }


    // CHANGE PASSWORD
    @Transactional
    public void changePassword(ChangePasswordRequestDTO dto) {
        UserEntity user = userRepository.findByEmailAndCpf(dto.email(), dto.cpf())
                .orElseThrow(() -> new UnauthorizedException("E-mail ou CPF inválidos."));

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    // ACTIVATE USER
    @Transactional
    public UserResponseDTO activateUser(UUID id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", id));
        ensureNotAdmin(user);

        user.setActive(true);
        user.setUpdatedAt(LocalDateTime.now());

        UserEntity updated = userRepository.save(user);

        return userMapper.toResponse(updated);
    }

    // INACTIVATE USER
    @Transactional
    public UserResponseDTO inactivateUser(UUID id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", id));
        ensureNotAdmin(user);
        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());

        UserEntity updated = userRepository.save(user);

        return userMapper.toResponse(updated);
    }

    private void ensureNotAdmin(UserEntity user) {
        if (user.isAdmin()) {
            throw new UnauthorizedException("Usuários administradores não podem ser editados ou inativados.");
        }
    }
}
