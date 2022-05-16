package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private UserService userService;

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
    public User getById(@PathVariable String id) {
        log.debug("Получен запрос GET /users.");
        System.out.println(id);
        return userService.getById(Long.parseLong(id));
    }

    //добавление в друзья PUT /users/{id}/friends/{friendId}
    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        System.out.println(id + " " + friendId);
        return userService.addFriend(id, friendId);
    }

    //удаление из друзей DELETE /users/{id}/friends/{friendId}
    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable String id, @PathVariable String friendId) {
        return userService.deleteFriend(Long.parseLong(id), Long.parseLong(friendId));
    }

    //возвращаем список друзей пользователя GET /users/{id}/friends
    @GetMapping("/{id}/friends")
    public List<User> getAllFriend(@PathVariable String id) {
        return userService.getAllFriends(Long.parseLong(id));
    }

    //список друзей, общих с другим пользователем GET /users/{id}/friends/common/{otherId}
    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<Long> getCommonUserFriend(@PathVariable String id, @PathVariable String otherId) {
        return userService.getCommonFriends(Long.parseLong(id), Long.parseLong(otherId));
    }

}
