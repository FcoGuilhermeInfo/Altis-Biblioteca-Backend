package com.altis.library.auth.services;

import com.altis.library.auth.dtos.ForgotPasswordRequestDTO;
import com.altis.library.auth.dtos.ResetPasswordRequestDTO;
import com.altis.library.shared.exception.BusinessExceptionException;
import com.altis.library.shared.exception.UnauthorizedException;
import com.altis.library.users.models.entities.UserEntity;
import com.altis.library.users.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final long tokenExpirationMillis;

    public PasswordResetService(
            UserRepository userRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            @Value("${password.reset-token-expiration:900000}") long tokenExpirationMillis) {
        if (tokenExpirationMillis <= 0) {
            throw new IllegalArgumentException(
                    "password.reset-token-expiration must be greater than zero."
            );
        }
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.tokenExpirationMillis = tokenExpirationMillis;
    }

    public String createToken(ForgotPasswordRequestDTO dto) {
        UserEntity user = userRepository.findByEmailAndCpf(dto.email(), dto.cpf())
                .orElseThrow(() -> new UnauthorizedException("E-mail ou CPF inválidos."));

        return jwtService.generatePasswordResetToken(user, tokenExpirationMillis);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequestDTO dto) {
        validatePasswordConfirmation(dto);
        UserEntity user = findUserForToken(dto.token());
        changePassword(user, dto.newPassword());
        userRepository.save(user);
    }

    private void validatePasswordConfirmation(ResetPasswordRequestDTO dto) {
        if (!dto.newPassword().equals(dto.confirmPassword())) {
            throw new BusinessExceptionException("A confirmação da senha não corresponde.");
        }
    }

    private UserEntity findUserForToken(String token) {
        UserEntity user = userRepository.findByEmail(extractEmail(token))
                .orElseThrow(() -> new UnauthorizedException("Token inválido ou expirado."));
        if (!jwtService.isPasswordResetTokenValid(token, user)) {
            throw new UnauthorizedException("Token inválido ou expirado.");
        }
        return user;
    }

    private String extractEmail(String token) {
        try {
            return jwtService.extractUsername(token);
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException exception) {
            throw new UnauthorizedException("Token inválido ou expirado.");
        }
    }

    private void changePassword(UserEntity user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        LocalDateTime nextUpdatedAt = user.getUpdatedAt().plusNanos(1_000_000);
        LocalDateTime now = LocalDateTime.now();
        user.setUpdatedAt(now.isAfter(nextUpdatedAt) ? now : nextUpdatedAt);
    }
}
