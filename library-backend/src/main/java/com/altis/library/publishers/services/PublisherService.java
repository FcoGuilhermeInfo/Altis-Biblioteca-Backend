package com.altis.library.publishers.services;

import com.altis.library.books.repositories.BookRepository;
import com.altis.library.publishers.models.dtos.PublisherRequestDTO;
import com.altis.library.publishers.models.dtos.PublisherResponseDTO;
import com.altis.library.publishers.models.dtos.PublisherUpdateDTO;
import com.altis.library.publishers.models.entities.Publisher;
import com.altis.library.publishers.repositories.PublisherRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;

    public PublisherService(PublisherRepository publisherRepository, BookRepository bookRepository) {
        this.publisherRepository = publisherRepository; this.bookRepository = bookRepository;
    }

    // CREATE
    @Transactional
    public PublisherResponseDTO create(PublisherRequestDTO dto){
        Publisher publisher = new Publisher();

        publisher.setName(dto.name());
        publisher.setEmail(dto.email());
        publisher.setPhone(dto.phone());
        publisher.setSite(dto.site());

        LocalDateTime now = LocalDateTime.now();
        publisher.setCreatedAt(now);
        publisher.setUpdatedAt(now);

        publisherRepository.save(publisher);

        return new PublisherResponseDTO(
                publisher.getId(),
                publisher.getName(),
                publisher.getEmail(),
                publisher.getPhone(),
                publisher.getSite(),
                publisher.getCreatedAt(),
                publisher.getUpdatedAt()
        );
    }

    // READ
    @Transactional
    public List<Publisher> findAll() {
        return publisherRepository.findAll();
    }

    // READ BY ID
    @Transactional
    public Publisher findById(UUID id){
        return publisherRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
    }

    // UPDATE
    @Transactional
    public PublisherResponseDTO update(UUID id, PublisherUpdateDTO dto){
        Publisher publisher = publisherRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (dto.name() != null){
            publisher.setName(dto.name());
        }
        if (dto.email() != null){
            publisher.setEmail(dto.email());
        }
        if (dto.phone() != null){
            publisher.setPhone(dto.phone());
        }
        if (dto.site() != null){
            publisher.setSite(dto.site());
        }

        publisher.setUpdatedAt(LocalDateTime.now());

        Publisher updated = publisherRepository.save(publisher);
        return new PublisherResponseDTO(
                updated.getId(),
                updated.getName(),
                updated.getEmail(),
                updated.getPhone(),
                updated.getSite(),
                updated.getCreatedAt(),
                updated.getUpdatedAt()
        );
    }

    // DELETE
    @Transactional
    public void delete(UUID id){
        Publisher publisher = publisherRepository.findById(id).orElseThrow(() -> new RuntimeException("Editora não encontrada."));

        if (bookRepository.existsByPublisherId(id)) {
            throw new RuntimeException("Não é possivel excluir a editora, pois existem livros relacionados a ela.");
        }

        publisherRepository.delete(publisher);
    }
}
