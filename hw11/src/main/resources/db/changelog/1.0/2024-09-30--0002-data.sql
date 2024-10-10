INSERT INTO authors(full_name)
VALUES ('George Orwell'), ('Jane Austen'), ('F. Scott Fitzgerald'), ('Harper Lee'),
       ('Ernest Hemingway'), ('J.K. Rowling'), ('Leo Tolstoy'), ('Agatha Christie');

INSERT INTO genres(name)
VALUES ('Science Fiction'), ('Romance'), ('Classic'), ('Mystery'), ('Adventure'),
       ('Fantasy'), ('Historical Fiction'), ('Crime');

INSERT INTO books(title, author_id)
VALUES ('1984', 1), ('Pride and Prejudice', 2), ('The Great Gatsby', 3),
       ('To Kill a Mockingbird', 4), ('The Old Man and the Sea', 5),
       ('Harry Potter and the Philosopher''s Stone', 6), ('War and Peace', 7),
       ('Murder on the Orient Express', 8), ('The Catcher in the Rye', 5),
       ('Crime and Punishment', 7), ('Harry Potter and the Chamber of Secrets', 6),
       ('Harry Potter and the Prisoner of Azkaban', 6), ('The Adventures of Sherlock Holmes', 8);

INSERT INTO comments(book_id, content)
VALUES (1, 'Recommended to read'),
       (2, 'Have already been read');

INSERT INTO books_genres(book_id, genre_id)
VALUES (1, 1), (2, 2), (2, 3), (3, 3), (4, 4), (5, 5), (5, 3),
       (6, 6), (7, 3), (7, 7), (8, 8), (9, 7), (9, 8), (10, 3), (10, 7),
       (11, 6), (11, 3), (12, 3), (12, 6), (13, 4), (13, 7);

INSERT INTO users(username, password, role)
VALUES ('admin', '$2y$10$fsBa.mRJCU7YUC9msaasXOyHQlVIWj0F6i5W.yeUpc7OF..dDNLWe', 'admin'), -- pwd
       ('user', '$2y$10$FD2GIs/aKP46U5aCD.jKyeJPbxanvN6Tk0NHO2jrBTsO/fvwMPQW.', 'user'); -- user