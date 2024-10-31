INSERT INTO authors(full_name)
VALUES ('Author_1'), ('Author_2'), ('Author_3');

INSERT INTO genres(name)
VALUES ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

INSERT INTO books(title, author_id)
VALUES ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

INSERT INTO book_genres(book_id, genre_id)
VALUES (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);