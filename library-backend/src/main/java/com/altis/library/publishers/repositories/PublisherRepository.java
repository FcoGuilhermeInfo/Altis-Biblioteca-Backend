package com.altis.library.publishers.repositories;

import com.altis.library.publishers.models.entities.PublisherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.Optional;

@Repository
public interface PublisherRepository extends JpaRepository<PublisherEntity, UUID> {

    Optional<PublisherEntity> findByNameIgnoreCase(String name);

    Page<PublisherEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
