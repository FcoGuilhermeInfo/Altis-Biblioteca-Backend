CREATE TABLE tb_loans (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    book_id UUID NOT NULL,
    borrowed_at TIMESTAMP NOT NULL,
    due_date TIMESTAMP NOT NULL,
    returned_at TIMESTAMP,

    CONSTRAINT fk_loans_user
        FOREIGN KEY (user_id)
        REFERENCES tb_users(id),

    CONSTRAINT fk_loans_book
        FOREIGN KEY (book_id)
        REFERENCES tb_books(id),

    CONSTRAINT chk_loans_dates
        CHECK (due_date >= borrowed_at)
);
