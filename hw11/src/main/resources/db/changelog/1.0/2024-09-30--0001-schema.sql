CREATE TABLE authors
(
    id IDENTITY PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL
);

CREATE TABLE genres
(
    id IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE books
(
    id IDENTITY PRIMARY KEY,
    title     VARCHAR(255) NOT NULL,
    author_id BIGINT       NOT NULL,
    FOREIGN KEY (author_id) REFERENCES authors (id)
);

CREATE TABLE comments
(
    id IDENTITY PRIMARY KEY,
    book_id BIGINT REFERENCES books (id) ON DELETE CASCADE,
    content VARCHAR(1024)
);

CREATE TABLE books_genres
(
    book_id  BIGINT NOT NULL,
    genre_id BIGINT NOT NULL,
    PRIMARY KEY (book_id, genre_id),
    FOREIGN KEY (book_id) REFERENCES books (id),
    FOREIGN KEY (genre_id) REFERENCES genres (id)
);

CREATE TABLE users
(
    id         IDENTITY PRIMARY KEY,
    username   VARCHAR(64),
    password   VARCHAR(1024),
    role       VARCHAR(255)
);