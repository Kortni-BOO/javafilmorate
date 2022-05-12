package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.error.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private HashMap<Long, User> users = new HashMap<>();
    private long id = 0;

    private long generateId() {
        return ++id;
    }

    //создание пользователя
    @Override
    @PostMapping
    public User create(@RequestBody User user) {
        checkData(user);
        if (users.containsKey(user.getEmail())) {
            throw new ValidationException(String.format(
                    "Пользователь с электронной почтой %s уже зарегистрирован.",
                    user.getEmail()
            ));
        }
        user.setId(generateId());
        log.debug("Получен запрос POST /users.");
        users.put(user.getId(), user);
        return user;
    }

    //обновление пользователя;
    @Override
    @PutMapping
    public User update(@RequestBody User user) {
        checkData(user);
        log.debug("Получен запрос PUT /users.");
        if(users.get(user.getId()) != null) {
            users.put(user.getId(), user);
        }
        return users.get(user.getId());
    }

    //получение списка всех пользователей
    @Override
    @GetMapping
    public List<User> findAll() {
        log.debug("Получен запрос GET /users.");
        List<User> usersList = new ArrayList<>(users.values());
        return usersList;
    }

    @Override
    public void checkData(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || (!user.getEmail().contains("@"))) {
            throw new ValidationException("Адрес электронной почты не может быть " +
                    "пустым и должен содержать символ @.");
        }
        if(user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }
}
