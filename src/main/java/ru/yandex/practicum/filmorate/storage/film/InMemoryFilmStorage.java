package ru.yandex.practicum.filmorate.storage.film;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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
    public Film create(Film film) {
        checkData(film);
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    //обновление фильма
    @Override
    public Film update(Film film) {
        checkData(film);
        if(films.get(film.getId()) != null) {
            films.put(film.getId(), film);
        }
        return films.get(film.getId());
    }

    //получение всех фильмов
    @Override
    public List<Film> findAll() {
        log.debug("Получен запрос GET /films.");
        List<Film> filmsList = new ArrayList<>(films.values());
        return filmsList;
    }

    //получить фильм по id
    @Override
    public Optional<Film> getById(long id) {
        //Optional<Film> optFilm = Optional.ofNullable(films.get(id));

        if(id < 0) {
            throw new UserNotFoundException("Фильм № %d не найден");
        }
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public void like(long id, long userId) {

    }

    @Override
    public void deleteLike(long id, long userId) {

    }

    @Override
    public List<Film> getHitMovie(int count) {
        return null;
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
