package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private RateSizeComparator comparator = new RateSizeComparator();

    @Autowired
    public FilmService(@Qualifier("filmDbStorage")FilmStorage filmStorage,
                       UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    //добавление фильма
    public Film create(Film film) {
        checkData(film);
        return filmStorage.create(film);
    }

    //обновление фильма
    public Film update(Film film) {
        checkData(film);
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
    public void like(long id, long userId) {
        filmStorage.like(id, userId);
    }
    //удаление лайка
    public void deleteLike(long id, long userId) {
        filmStorage.deleteLike(id, userId);
    }
    //вывод 10 наиболее популярных фильмов по количеству лайков
    public List<Film> getHitMovie(int count) {
        return filmStorage.getHitMovie(count);
    }

    public void checkData(Film film) {
        if(film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if(film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания - 200 символов.");
        }
        if(film.getDescription().isBlank()) {
            throw new ValidationException("Должно быть описание");
        }
        if(film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            throw new ValidationException("Дата релиза - не раньше 28 декабря 1895.");
        }
        if(film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
    }
}
