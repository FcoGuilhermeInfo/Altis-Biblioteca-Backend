package com.altis.library.users.controllers;

import com.altis.library.users.models.dtos.UserRequestDTO;
import com.altis.library.users.models.dtos.UserResponseDTO;
import com.altis.library.users.models.dtos.UserUpdateDTO;
import com.altis.library.users.models.dtos.ChangePasswordRequestDTO;
import com.altis.library.users.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO create(@Valid @RequestBody UserRequestDTO dto) {
        return userService.create(dto);
    }

    // CHANGE PASSWORD
    @PostMapping("/change-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@Valid @RequestBody ChangePasswordRequestDTO dto) {
        userService.changePassword(dto);
    }

    // READ
    @GetMapping
    public Page<UserResponseDTO> findAll(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return userService.findAll(search, PageRequest.of(page, size));
    }

    // READ BY ID
    @GetMapping("/{id}")
    public UserResponseDTO findById(@PathVariable UUID id){
        return userService.findById(id);
    }

    // UPDATE
    @PatchMapping("/{id}")
    public UserResponseDTO update(@PathVariable UUID id, @Valid @RequestBody UserUpdateDTO dto){
        return userService.update(id,dto);
    }

    // ACTIVATE
    @PatchMapping("/{id}/activate")
    public UserResponseDTO activate(@PathVariable UUID id){
        return userService.activateUser(id);
    }

    // INACTIVATE
    @PatchMapping("/{id}/inactivate")
    public UserResponseDTO inactivate(@PathVariable UUID id){
        return userService.inactivateUser(id);
    }





}
