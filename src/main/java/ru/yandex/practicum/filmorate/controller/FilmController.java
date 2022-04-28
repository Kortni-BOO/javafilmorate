package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.error.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private HashMap<Integer, Film> films = new HashMap<>();
    //добавление фильма
    @PostMapping
    public Film create(@RequestBody Film film) {
        checkTitle(film);
        checkDescription(film);
        checkReleaseData(film);
        checkDuration(film);
        log.debug("Получен запрос POST /films.");
        films.put(film.getId(), film);
        return film;
    }
    //обновление фильма
    @PutMapping
    public Film update(@RequestBody Film film) {
        log.debug("Получен запрос PUT /films.");
        checkReleaseData(film);
        films.put(film.getId(), film);
        return films.get(film.getId());
    }
    //получение всех фильмов
    @GetMapping
    public Collection<Film> findAll() {
        log.debug("Получен запрос GET /films.");
        return films.values();
    }

    public void checkTitle(Film film) {
        if(film.getName() == null) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
    }

    public void checkDescription(Film film) {
        if(film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания - 200 символов.");
        }
    }

    public void checkReleaseData(Film film) {
        if(film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            throw new ValidationException("Дата релиза - не раньше 28 декабря 1895.");
        }
    }

    public void checkDuration(Film film) {
        if(film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
    }

}
