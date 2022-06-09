INSERT INTO mpa (name) VALUES ('G'), ('PG'), ('PG-13'), ('R'), ('NC-17');

INSERT INTO users (email, login, name, birthday) VALUES ('kis@mail.ru', 'kos', 'kis kis', '1990-12-20');
INSERT INTO users (email, login, name, birthday) VALUES ('hlo@email.ru', 'Hlo', 'Hlo Hlo', '2020-09-6');
INSERT INTO users (email, login, name, birthday) VALUES ('rr@email.ru', 'romashka', 'Rr Romashka', '1994-02-06');

INSERT INTO friends (user_id, friend_id) VALUES (1, 2);
INSERT INTO friends (user_id, friend_id) VALUES (1, 3);
INSERT INTO friends (user_id, friend_id) VALUES (2, 3);


INSERT INTO film (name, description, release_date, duration, mpaa_id) VALUES ('Я - начало', 'Духовное начало', '2014-9-25', 108, 1);
INSERT INTO film (name, description, release_date, duration, mpaa_id) VALUES ('Красота по-американски', 'клише', '2000-03-22', 122, 4);

INSERT INTO `like` (user_id, film_id) VALUES (1, 2);