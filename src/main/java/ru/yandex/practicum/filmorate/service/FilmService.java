package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    FilmStorage filmStorage = new InMemoryFilmStorage();
    UserService userService = new UserService();

    //добавление фильма
    public Film create(@RequestBody Film film) {
        return filmStorage.create(film);
    }

    //обновление фильма
    public Film update(@RequestBody Film film) {
        return filmStorage.update(film);
    }

    public Film getById(long id) {
        return filmStorage.getById(id);
    }

    //получение всех фильмов
    public List<Film> findAll() {
        List<Film> filmsList = new ArrayList<>(filmStorage.findAll());
        return filmsList;
    }
    //добавление лайка
    public Film like(long id, long filmId) {
        User user = userService.getById(filmId);
        Film film = filmStorage.getById(id);
        film.addLike(user.getId());
        return film;
    }
    //удаление лайка
    public Film deleteLike(long id, long filmId) {
        User user = userService.getById(filmId);
        Film film = filmStorage.getById(id);
        film.getRate().remove(user.getId());
        return film;
    }
    //вывод 10 наиболее популярных фильмов по количеству лайков
    public List<Film> getHitMovie(int count) {
        RateSizeComparator comparator = new RateSizeComparator();
        List<Film> hitFilms = filmStorage.findAll().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
        if (count > hitFilms.size() && hitFilms.size() < 10) {
            return hitFilms;
        } else if (count > hitFilms.size()) {
            return hitFilms.stream()
                    .limit(10)
                    .collect(Collectors.toList());
        } else {
            return hitFilms.stream()
                    .limit(count)
                    .collect(Collectors.toList());
        }
    }
}
