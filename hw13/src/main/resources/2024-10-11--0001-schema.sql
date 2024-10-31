CREATE TABLE authors
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255)
);

CREATE TABLE genres
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE books
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    title     VARCHAR(255),
    author_id BIGINT,
    FOREIGN KEY (author_id) REFERENCES authors (id)
);

CREATE TABLE book_genres
(
    book_id  BIGINT,
    genre_id BIGINT,
    FOREIGN KEY (book_id) REFERENCES books (id),
    FOREIGN KEY (genre_id) REFERENCES genres (id)
);