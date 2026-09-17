package com.altis.library.books.repositories;

import com.altis.library.books.models.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {


    boolean existsByTitleAndAuthorAndReleaseYearAndPublisher_Id(
            String title, String author, Short releaseYear, UUID publisherId);

}
