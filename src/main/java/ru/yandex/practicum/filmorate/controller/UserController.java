package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //создание пользователя
    @PostMapping
    public User create(@RequestBody User user) {
        log.debug("Получен запрос POST /users.");
        return userService.create(user);
    }

    //обновление пользователя;
    @PutMapping
    public User update(@RequestBody User user) {
        log.debug("Получен запрос PUT /users.");
        return userService.update(user);
    }

    //получение списка всех пользователей
    @GetMapping
    public List<User> findAll() {
        log.debug("Получен запрос GET /users.");
        return userService.findAll();
    }

    //получение пользователя по id
    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        log.debug("Получен запрос GET /users.");
        System.out.println(id);
        return userService.getById(id);
    }

    //добавление в друзья PUT /users/{id}/friends/{friendId}
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
    }

    //удаление из друзей DELETE /users/{id}/friends/{friendId}
    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.deleteFriend(id, friendId);
    }

    //возвращаем список друзей пользователя GET /users/{id}/friends
    @GetMapping("/{id}/friends")
    public List<User> getAllFriend(@PathVariable Long id) {
        return userService.getAllFriends(id);
    }

    //список друзей, общих с другим пользователем GET /users/{id}/friends/common/{otherId}
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonUserFriend(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getCommonFriends(id, otherId);
    }

}
