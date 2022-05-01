package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.error.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private HashMap<Long, Film> films = new HashMap<>();
    private static long id = 0;

    private long generateId() {
        return ++id;
    }
    //добавление фильма
    @PostMapping
    public Film create(@RequestBody Film film) {
        checkData(film);
        log.debug("Получен запрос POST /films.");
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }
    //обновление фильма
    @PutMapping
    public Film update(@RequestBody Film film) {
        log.debug("Получен запрос PUT /films.");
        checkData(film);
        films.put(film.getId(), film);
        return films.get(film.getId());
    }
    //получение всех фильмов
    @GetMapping
    public List<Film> findAll() {
        log.debug("Получен запрос GET /films.");
        List<Film> filmsList = new ArrayList<>(films.values());
        return filmsList;
    }

    public void checkData(Film film) {
        if(film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if(film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания - 200 символов.");
        }
        if(film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            throw new ValidationException("Дата релиза - не раньше 28 декабря 1895.");
        }
        if(film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
    }
}
