package com.altis.library.users.services;

import com.altis.library.users.models.dtos.UserRequestDTO;
import com.altis.library.users.models.dtos.UserResponseDTO;
import com.altis.library.users.models.dtos.UserUpdateDTO;
import com.altis.library.users.models.entities.UserEntity;
import com.altis.library.users.mappers.UserMapper;
import com.altis.library.users.repositories.UserRepository;
import com.altis.library.shared.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public List<UserResponseDTO> findAll() {
        return userMapper.toResponseList(userRepository.findAll());
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

    // UPDATE
    @Transactional
    public UserResponseDTO update(UUID id, UserUpdateDTO dto) {

        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        userMapper.applyUpdate(dto, user);

        var updated = userRepository.save(user);
        return userMapper.toResponse(updated);
    }

    // DELETE
    @Transactional
    public void delete(UUID id) {

        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        userRepository.delete(user);
    }
}
