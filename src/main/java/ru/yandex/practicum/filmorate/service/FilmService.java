package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.io.File;

@Service
public class FilmService {

    //добавление лайка
    public void like(User user, Film film) {
        film.addLike(user.getId());
    }
    //удаление лайка
    public void deleteLike(User user, Film film) {
        film.getLikedCount().remove(user.getId());
    }
    //вывод 10 наиболее популярных фильмов по количеству лайков
}
