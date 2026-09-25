CREATE TABLE tb_books (
    id UUID PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    author VARCHAR(100) NOT NULL,
    release_year SMALLINT NOT NULL,
    publisher_id UUID NOT NULL,
    total_quantity INTEGER NOT NULL,
    borrowed_quantity INTEGER NOT NULL DEFAULT 0,
    available_quantity INTEGER GENERATED ALWAYS AS (total_quantity - borrowed_quantity) STORED,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_books_publisher
        FOREIGN KEY (publisher_id)
        REFERENCES tb_publishers(id),

    CONSTRAINT uq_book
        UNIQUE (title, author, release_year, publisher_id),

    CONSTRAINT chk_books_total_quantity
        CHECK (total_quantity > 0),

    CONSTRAINT chk_books_borrowed_quantity
        CHECK (borrowed_quantity >= 0),

    CONSTRAINT chk_books_quantity
        CHECK (total_quantity >= borrowed_quantity)
);