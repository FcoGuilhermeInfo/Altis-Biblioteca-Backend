package com.altis.library.users.controllers;

import com.altis.library.users.models.dtos.UserRequestDTO;
import com.altis.library.users.models.dtos.UserResponseDTO;
import com.altis.library.users.models.dtos.UserUpdateDTO;
import com.altis.library.users.models.dtos.ChangePasswordRequestDTO;
import com.altis.library.users.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @PostMapping("/change-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@Valid @RequestBody ChangePasswordRequestDTO dto) {
        userService.changePassword(dto);
    }

    // READ
    @GetMapping
    public List<UserResponseDTO> findAll(){
        return userService.findAll();
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

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id){
        userService.delete(id);
    }




}
