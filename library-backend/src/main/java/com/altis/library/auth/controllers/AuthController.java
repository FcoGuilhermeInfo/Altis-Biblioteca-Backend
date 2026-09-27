package com.altis.library.auth.controllers;

import com.altis.library.auth.dtos.ForgotPasswordRequestDTO;
import com.altis.library.auth.dtos.LoginRequestDTO;
import com.altis.library.auth.dtos.LoginResponseDTO;
import com.altis.library.auth.dtos.PasswordResetTokenResponseDTO;
import com.altis.library.auth.dtos.ResetPasswordRequestDTO;
import com.altis.library.auth.services.AuthService;
import com.altis.library.auth.services.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;

    public AuthController(AuthService authService, PasswordResetService passwordResetService) {
        this.authService = authService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO dto) {
        return authService.login(dto);
    }

    @PostMapping("/forgot-password")
    public PasswordResetTokenResponseDTO forgotPassword(
            @Valid @RequestBody ForgotPasswordRequestDTO dto) {
        return new PasswordResetTokenResponseDTO(
                    passwordResetService.createToken(dto)
        );
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@Valid @RequestBody ResetPasswordRequestDTO dto) {
        passwordResetService.resetPassword(dto);
    }
}
