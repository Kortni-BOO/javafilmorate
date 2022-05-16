package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.error.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    //добавление фильма
    @PostMapping
    public Film create(@RequestBody Film film) {
        log.debug("Получен запрос POST /films.");
        return service.create(film);
    }

    //обновление фильма
    @PutMapping
    public Film update(@RequestBody Film film) {
        log.debug("Получен запрос PUT /films.");
        return service.update(film);
    }

    //получение всех фильмов
    @GetMapping
    public List<Film> findAll() {
        log.debug("Получен запрос GET /films.");
        List<Film> filmsList = new ArrayList<>(service.findAll());
        return filmsList;
    }

    //пользователь ставит лайк фильму PUT /films/{id}/like/{userId}
    @PutMapping("/{id}/like/{}userId")
    public Film setLike(@PathVariable String id, @PathVariable String userId) {
        return service.like(Long.parseLong(id), Long.parseLong(userId));
    }
    //пользователь удаляет лайк DELETE /films/{id}/like/{userId}
    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable String id, @PathVariable String userId) {
        return service.deleteLike(Long.parseLong(id), Long.parseLong(userId));
    }
    /*
    GET /films/popular?count={count}
    возвращает список из первых count фильмов по количеству лайков.
    Если значение параметра count не задано, верните первые 10
    */
    @GetMapping("/popular?count={count}")
    public List<Film> getPopular(@PathVariable String count) {
        return service.getHitMovie(Integer.parseInt(count));
    }
}
