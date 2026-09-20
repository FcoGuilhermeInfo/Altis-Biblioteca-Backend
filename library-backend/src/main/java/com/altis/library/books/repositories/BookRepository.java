package com.altis.library.books.repositories;

import com.altis.library.books.models.entities.BookEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, UUID> {


    boolean existsByTitleAndAuthorAndReleaseYearAndPublisher_Id(
            String title, String author, Short releaseYear, UUID publisherId);

    boolean existsByPublisherId(UUID publisherId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select book from BookEntity book where book.id = :id")
    Optional<BookEntity> findByIdForUpdate(@Param("id") UUID id);

}
