insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);

insert into comments(book_id, content)
values (1, 'Comment_1_for_book_1'), (1, 'Comment_2_for_book_1'),
       (2, 'Comment_1_for_book_2'), (2, 'Comment_2_for_book_2'),
       (3, 'Comment_1_for_book_3'), (3, 'Comment_2_for_book_3');

insert into users(username, password, role)
values ('admin', '$2y$10$fsBa.mRJCU7YUC9msaasXOyHQlVIWj0F6i5W.yeUpc7OF..dDNLWe', 'admin'), -- pwd
       ('user', '$2y$10$FD2GIs/aKP46U5aCD.jKyeJPbxanvN6Tk0NHO2jrBTsO/fvwMPQW.', 'user') -- user