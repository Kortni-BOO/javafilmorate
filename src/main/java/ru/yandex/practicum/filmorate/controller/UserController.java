package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.error.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private HashMap<String, User> users = new HashMap<>();

    //создание пользователя
    @PostMapping
    public User create(@RequestBody User user) {
        checkEmail(user);
        checkLogin(user);
        checkdateOfBirth(user);
        if (users.containsKey(user.getEmail())) {
            throw new ValidationException(String.format(
                    "Пользователь с электронной почтой %s уже зарегистрирован.",
                    user.getEmail()
            ));
        }
        log.debug("Получен запрос POST /users.");
        users.put(user.getEmail(), user);
        return user;
    }
    //обновление пользователя;
    @PutMapping
    public User update(@RequestBody User user) {
        log.debug("Получен запрос PUT /users.");
        users.put(user.getEmail(), user);
        return users.get(user.getEmail());
    }
    //получение списка всех пользователей
    @GetMapping
    public Collection<User> findAll() {
        log.debug("Получен запрос GET /users.");
        return users.values();
    }

    private void checkEmail(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || (!user.getEmail().contains("@"))) {
            throw new ValidationException("Адрес электронной почты не может быть " +
                    "пустым и должен содержать символ @.");
        }
    }

    private void checkLogin(User user) {
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
    }

    private void checkdateOfBirth(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }
}
