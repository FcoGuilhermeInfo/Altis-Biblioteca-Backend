package com.altis.library.publishers.controllers;

import com.altis.library.publishers.models.dtos.PublisherRequestDTO;
import com.altis.library.publishers.models.dtos.PublisherResponseDTO;
import com.altis.library.publishers.services.PublisherService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    public List<PublisherResponseDTO> findAll(){
        return publisherService.findAll();
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
