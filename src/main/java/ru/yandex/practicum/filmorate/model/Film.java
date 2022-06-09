package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class Film {
    private long id;//целочисленный идентификатор
    private String name;//название
    private String description;//описание
    private LocalDate releaseDate;//дата релиза
    private long duration;//продолжительность фильма
    private Mpaa mpaa;
    @JsonIgnore
    private Set<Long> rate = new HashSet<>();
    public void addLike(long id) {
        rate.add(id);
    }

}
