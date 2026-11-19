
package com.altis.library.users.controllers;

import com.altis.library.users.models.dtos.UserChangePasswordDTO;
import com.altis.library.users.models.dtos.UserRequestDTO;
import com.altis.library.users.models.dtos.UserResponseDTO;
import com.altis.library.users.models.dtos.UserUpdateDTO;
import com.altis.library.users.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO create(
            @Valid @RequestBody UserRequestDTO dto
    ) {
        return userService.create(dto);
    }

    @GetMapping
    public Page<UserResponseDTO> findAll(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "1")
            @Min(1) int page,
            @RequestParam(defaultValue = "10")
            @Min(1) @Max(100) int size
    ) {
        return userService.findAll(
                search,
                PageRequest.of(page - 1, size)
        );
    }

    @GetMapping("/{id}")
    public UserResponseDTO findById(
            @PathVariable UUID id
    ) {
        return userService.findById(id);
    }

    @PatchMapping("/{id}")
    public UserResponseDTO update(
            @PathVariable UUID id,
            @Valid @RequestBody UserUpdateDTO dto
    ) {
        return userService.update(id, dto);
    }

    @PatchMapping("/me/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(
            Authentication authentication,
            @Valid @RequestBody UserChangePasswordDTO dto
    ) {
        userService.changePassword(authentication, dto);
    }

    @PatchMapping("/{id}/activate")
    public UserResponseDTO activate(
            @PathVariable UUID id
    ) {
        return userService.activateUser(id);
    }

    @PatchMapping("/{id}/inactivate")
    public UserResponseDTO inactivate(
            @PathVariable UUID id
    ) {
        return userService.inactivateUser(id);
    }
}
