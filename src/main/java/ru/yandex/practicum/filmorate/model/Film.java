package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Film {
    private long id;//целочисленный идентификатор
    private String name;//название
    private String description;//описание
    private LocalDate releaseDate;//дата релиза
    private long duration;//продолжительность фильма

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

}
