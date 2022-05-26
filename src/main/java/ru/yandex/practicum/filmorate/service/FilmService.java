package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private RateSizeComparator comparator = new RateSizeComparator();

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    //добавление фильма
    public Film create(Film film) {
        return filmStorage.create(film);
    }

    //обновление фильма
    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film getById(long id) {
        Film film = filmStorage.getById(id)
                .orElseThrow(() -> new UserNotFoundException("Фильм не найден."));
        return film;
    }

    //получение всех фильмов
    public List<Film> findAll() {
        return filmStorage.findAll();
    }
    //добавление лайка
    public Film like(long id, long userId) {
        User user = userService.getById(userId);
        Film film = filmStorage.getById(id).get();
        //film.addLike(user.getId());
        film.getRate().add(userId);
        return film;
    }
    //удаление лайка
    public Film deleteLike(long id, long userId) {
        User user = userService.getById(userId);
        Film film = filmStorage.getById(id).get();
        film.getRate().remove(user.getId());
        return film;
    }
    //вывод 10 наиболее популярных фильмов по количеству лайков
    public List<Film> getHitMovie(int count) {
        List<Film> hitFilms = filmStorage.findAll().stream()
                .sorted(comparator.reversed())
                .limit(count)
                .collect(Collectors.toList());
        return hitFilms;
    }
}
