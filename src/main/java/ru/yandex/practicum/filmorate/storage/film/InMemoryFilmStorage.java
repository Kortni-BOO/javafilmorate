package ru.yandex.practicum.filmorate.storage.film;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.error.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Getter
@Setter
public class InMemoryFilmStorage implements FilmStorage {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private HashMap<Long, Film> films = new HashMap<>();
    private long id = 0;

    private long generateId() {
        return ++id;
    }

    //добавление фильма
    @Override
    @PostMapping
    public Film create(@RequestBody Film film) {
        checkData(film);
        log.debug("Получен запрос POST /films.");
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    //обновление фильма
    @Override
    @PutMapping
    public Film update(@RequestBody Film film) {
        log.debug("Получен запрос PUT /films.");
        checkData(film);
        if(films.get(film.getId()) != null) {
            films.put(film.getId(), film);
        }
        return films.get(film.getId());
    }

    //получение всех фильмов
    @Override
    @GetMapping
    public List<Film> findAll() {
        log.debug("Получен запрос GET /films.");
        List<Film> filmsList = new ArrayList<>(films.values());
        return filmsList;
    }

    @Override
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
