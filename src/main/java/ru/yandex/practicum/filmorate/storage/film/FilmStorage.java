package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    //добавление фильма
    public Film create(Film film);

    //обновление фильма
    public Film update(Film film);

    //получение всех фильмов
    public List<Film> findAll();

    //Поиск по id
    public Optional<Film> getById(long id);

    public void checkData(Film film);
}
