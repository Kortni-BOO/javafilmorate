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

    //добавление фильма
    public Film create(@RequestBody Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    //обновление фильма
    public Film update(@RequestBody Film film) {
        if(films.get(film.getId()) != null) {
            films.put(film.getId(), film);
        }
        return films.get(film.getId());
    }

    //получение всех фильмов
    public List<Film> findAll() {
        List<Film> filmsList = new ArrayList<>(films.values());
        return filmsList;
    }
    //добавление лайка
    public void like(User user, Film film) {
        film.addLike(user.getId());
    }
    //удаление лайка
    public void deleteLike(User user, Film film) {
        film.getRate().remove(user.getId());
    }
    //вывод 10 наиболее популярных фильмов по количеству лайков
    public List<Film> getHitMovie(int count) {
        RateSizeComparator comparator = new RateSizeComparator();
        List<Film> hitFilms = filmStorage.getFilms().values().stream()
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
