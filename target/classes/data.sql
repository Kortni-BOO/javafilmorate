//INSERT INTO `MPA` (name) VALUES ('G'), ('PG'), ('PG-13'), ('R'), ('NC-17');
INSERT INTO `GENRE` (name)
VALUES ('Мелодрама'), ('Триллер'), ('Комедия'), ('Фантастика'), ('Ужасы')


/*
SELECT f.name, f.description, f.release_date, f.duration, f.mpaa_id
FROM film AS f
LEFT JOIN `LIKE` AS l ON f.id = l.film_id
GROUP BY f.ID
ORDER BY COUNT(l.user_id)
LIMIT 10;
*/


