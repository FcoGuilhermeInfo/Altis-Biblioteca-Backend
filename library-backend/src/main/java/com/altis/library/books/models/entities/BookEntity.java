package com.altis.library.books.models.entities;

import com.altis.library.publishers.models.entities.Publisher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_books",
        uniqueConstraints = {
        @UniqueConstraint(name = "uq_book", columnNames = {"title", "author", "release_year", "publisher_id"})}
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 100)
    private String author;

    @Column(name = "release_year", nullable = false)
    private Short releaseYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "publisher_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_books_publisher")
    )
    private Publisher publisher;

    @Column(name = "total_quantity", nullable = false)
    private Integer totalQuantity;

    @Column(name = "borrowed_quantity", nullable = false)
    private Integer borrowedQuantity;

    @Column(
            name = "available_quantity",
            insertable = false,
            updatable = false
    )
    private Integer availableQuantity;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;


}
