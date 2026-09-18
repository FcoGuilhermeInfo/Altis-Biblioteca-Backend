package com.altis.library.publishers.services;

import com.altis.library.publishers.mappers.PublisherMapper;
import com.altis.library.publishers.models.dtos.PublisherRequestDTO;
import com.altis.library.publishers.models.dtos.PublisherResponseDTO;
import com.altis.library.publishers.models.entities.Publisher;
import com.altis.library.publishers.repositories.PublisherRepository;
import com.altis.library.shared.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final PublisherRepository publisherRepository;
    private final PublisherMapper publisherMapper;

    public Publisher findByName(String name) {
        return publisherRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", name));
    }

    @Transactional
    public PublisherResponseDTO create(PublisherRequestDTO dto) {
        Publisher publisher = publisherMapper.toEntity(dto);
        Publisher savedPublisher = publisherRepository.save(publisher);

        return publisherMapper.toResponse(savedPublisher);
    }

    public PublisherResponseDTO findById(UUID id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", id));

        return publisherMapper.toResponse(publisher);
    }

    public List<PublisherResponseDTO> findAll() {
        return publisherMapper.toResponseList(publisherRepository.findAll());
    }

    @Transactional
    public PublisherResponseDTO update(UUID id, PublisherRequestDTO dto) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", id));

        publisherMapper.applyUpdate(dto, publisher);
        Publisher updatedPublisher = publisherRepository.save(publisher);

        return publisherMapper.toResponse(updatedPublisher);
    }

    @Transactional
    public void delete(UUID id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", id));

        publisherRepository.delete(publisher);
    }
}
