package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;

public class RateSizeComparator implements Comparator<Film> {
    @Override
    public int compare(Film film1, Film film2) {
        return film1.getRate().size() - film2.getRate().size();
    }
}
