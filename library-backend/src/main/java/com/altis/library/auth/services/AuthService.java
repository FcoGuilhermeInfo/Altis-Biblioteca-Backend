package com.altis.library.auth.services;

import com.altis.library.auth.dtos.LoginRequestDTO;
import com.altis.library.auth.dtos.LoginResponseDTO;
import com.altis.library.shared.exception.UnauthorizedException;
import com.altis.library.users.models.entities.UserEntity;
import com.altis.library.users.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final long expiration;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       JwtService jwtService,
                       @org.springframework.beans.factory.annotation.Value("${jwt.expiration}") long expiration) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.expiration = expiration;
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.email(), dto.password()
                    )
            );
        } catch (AuthenticationException exception) {
            throw new UnauthorizedException("E-mail ou senha inválidos.");
        }

        UserEntity user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new UnauthorizedException(
                        "E-mail ou senha inválidos."
                ));

        return new LoginResponseDTO(
                jwtService.generateToken(user),
                "Bearer",
                expiration
        );
    }
}
