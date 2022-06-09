package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Mpaa {
    private int id;
    private String name;

    public Mpaa(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
