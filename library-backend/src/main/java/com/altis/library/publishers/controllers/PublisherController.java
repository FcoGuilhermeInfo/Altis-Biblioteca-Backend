package com.altis.library.publishers.controllers;

import com.altis.library.publishers.models.dtos.PublisherRequestDTO;
import com.altis.library.publishers.models.dtos.PublisherResponseDTO;
import com.altis.library.publishers.models.dtos.PublisherUpdateDTO;
import com.altis.library.publishers.models.entities.Publisher;
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
    public List<Publisher> findAll(){
        return publisherService.findAll();
    }

    @GetMapping("/{id}")
    public Publisher findById(@PathVariable UUID id){
        return publisherService.findById(id);
    }

    @PatchMapping("/{id}")
    public PublisherResponseDTO update(@PathVariable UUID id, @Valid @RequestBody PublisherUpdateDTO dto){
        return publisherService.update(id, dto);
    }
}
