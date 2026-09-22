package com.altis.library.publishers.services;

import com.altis.library.books.repositories.BookRepository;
import com.altis.library.publishers.mappers.PublisherMapper;
import com.altis.library.publishers.models.dtos.PublisherRequestDTO;
import com.altis.library.publishers.models.dtos.PublisherResponseDTO;
import com.altis.library.publishers.models.entities.PublisherEntity;
import com.altis.library.publishers.repositories.PublisherRepository;
import com.altis.library.shared.exception.ConflictException;
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
    private final BookRepository bookRepository;
    private final PublisherMapper publisherMapper;

    public PublisherEntity findByName(String name) {
        return publisherRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", name));
    }

    @Transactional
    public PublisherResponseDTO create(PublisherRequestDTO dto) {
        PublisherEntity publisherEntity = publisherMapper.toEntity(dto);
        PublisherEntity savedPublisherEntity = publisherRepository.save(publisherEntity);

        return publisherMapper.toResponse(savedPublisherEntity);
    }

    public PublisherResponseDTO findById(UUID id) {
        PublisherEntity publisherEntity = publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", id));

        return publisherMapper.toResponse(publisherEntity);
    }

    public List<PublisherResponseDTO> findAll() {
        return publisherMapper.toResponseList(publisherRepository.findAll());
    }

    @Transactional
    public PublisherResponseDTO update(UUID id, PublisherRequestDTO dto) {
        PublisherEntity publisherEntity = publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", id));

        publisherMapper.applyUpdate(dto, publisherEntity);
        PublisherEntity updatedPublisherEntity = publisherRepository.save(publisherEntity);

        return publisherMapper.toResponse(updatedPublisherEntity);
    }

    @Transactional
    public void delete(UUID id) {
        PublisherEntity publisherEntity = publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", id));

        if (bookRepository.existsByPublisherEntity_Id(id)) {
            throw new ConflictException(
                    "Não é possível excluir uma editora que possui livros relacionados."
            );
        }

        publisherRepository.delete(publisherEntity);
    }
}
