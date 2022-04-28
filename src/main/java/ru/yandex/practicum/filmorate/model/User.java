package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {
    private int id;//целочисленный идентификатор;
    private String email;//электронная почта;
    private String login;//логин пользователя;
    private String name;//имя для отображения;
    private LocalDate birthday;//дата рождения

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        if(name == null) {
            this.name = login;
        } else {
            this.name = name;
        }
        this.birthday = birthday;
    }

}
