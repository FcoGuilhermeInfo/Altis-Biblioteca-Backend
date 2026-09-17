package com.altis.library.publishers.repositories;

import com.altis.library.publishers.models.entities.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.Optional;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, UUID> {

    Optional<Publisher> findByNameIgnoreCase(String name);
}
