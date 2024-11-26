CREATE TABLE authors
(
    id        identity PRIMARY KEY,
    full_name VARCHAR(255)
);

CREATE TABLE genres
(
    id   identity PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE books
(
    id        identity PRIMARY KEY,
    title     VARCHAR(255),
    author_id BIGINT REFERENCES authors (id)
);

CREATE TABLE comments
(
    id      identity PRIMARY KEY,
    book_id BIGINT REFERENCES books (id) ON DELETE CASCADE,
    content VARCHAR(1024)
);

CREATE TABLE books_genres
(
    book_id  BIGINT REFERENCES books (id),
    genre_id BIGINT REFERENCES genres (id),
    PRIMARY KEY (book_id, genre_id)
);

