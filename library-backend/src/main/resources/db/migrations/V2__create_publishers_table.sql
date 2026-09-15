CREATE TABLE tb_publishers (
    id UUID primary key,
    name VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL ,
    phone VARCHAR(15) UNIQUE NOT NULL,
    site VARCHAR(80) UNIQUE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
)