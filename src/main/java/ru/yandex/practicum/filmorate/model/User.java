package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {
    private long id;//целочисленный идентификатор;
    private String email;//электронная почта;
    private String login;//логин пользователя;
    private String name;//имя для отображения;
    private LocalDate birthday;//дата рождения
    private Set<Long> friends;//список друзей

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public void addFriends(long id) {
        friends.add(id);
    }

}
