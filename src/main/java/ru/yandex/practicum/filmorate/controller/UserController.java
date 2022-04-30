package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.error.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private HashMap<Long, User> users = new HashMap<>();
    private static long id = 0;

    private long generateId() {
        return ++id;
    }
    //создание пользователя
    @PostMapping
    public User create(@RequestBody User user) {
        checkData(user);
        if (users.containsKey(user.getEmail())) {
            throw new ValidationException(String.format(
                    "Пользователь с электронной почтой %s уже зарегистрирован.",
                    user.getEmail()
            ));
        }
        if(user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(generateId());
        log.debug("Получен запрос POST /users.");
        users.put(user.getId(), user);
        return user;
    }
    //обновление пользователя;
    @PutMapping
    public User update(@RequestBody User user) {
        checkData(user);
        log.debug("Получен запрос PUT /users.");
        users.put(user.getId(), user);
        return users.get(user.getId());
    }
    //получение списка всех пользователей
    @GetMapping
    public List<User> findAll() {
        log.debug("Получен запрос GET /users.");
        List<User> usersList = new ArrayList<>(users.values());
        return usersList;
    }

    private void checkData(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || (!user.getEmail().contains("@"))) {
            throw new ValidationException("Адрес электронной почты не может быть " +
                    "пустым и должен содержать символ @.");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }

}
