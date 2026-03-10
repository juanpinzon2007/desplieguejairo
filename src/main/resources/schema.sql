CREATE TABLE IF NOT EXISTS games (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(120) NOT NULL,
    studio VARCHAR(100) NOT NULL,
    genre VARCHAR(60) NOT NULL,
    platform VARCHAR(60) NOT NULL,
    release_year INTEGER NOT NULL,
    rating NUMERIC(3,1) NOT NULL,
    available BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
