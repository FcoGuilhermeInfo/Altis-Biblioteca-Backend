package com.altis.library.publishers.controllers;

import com.altis.library.publishers.models.dtos.PublisherRequestDTO;
import com.altis.library.publishers.models.dtos.PublisherResponseDTO;
import com.altis.library.publishers.services.PublisherService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/publishers")
public class PublisherController {
    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @PostMapping
    public PublisherResponseDTO create(@Valid @RequestBody PublisherRequestDTO dto){
        return publisherService.create(dto);
    }

    @GetMapping
    public Page<PublisherResponseDTO> findAll(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size){
        return publisherService.findAll(search, PageRequest.of(page - 1, size));
    }

    @GetMapping("/{id}")
    public PublisherResponseDTO findById(@PathVariable UUID id){
        return publisherService.findById(id);
    }

    @PatchMapping("/{id}")
    public PublisherResponseDTO update(@PathVariable UUID id, @Valid @RequestBody PublisherRequestDTO dto){
        return publisherService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id){
        publisherService.delete(id);
    }
}
