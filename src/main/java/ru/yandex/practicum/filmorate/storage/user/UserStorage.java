package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    //создание пользователя
    public User create(@RequestBody User user);

    //обновление пользователя;
    public User update(@RequestBody User user);

    //получение списка всех пользователей
    public List<User> findAll();

    //Поиск по id
    public Optional<User> getById(long id);

    public void addFriend(long id, long friendId);

    public void deleteFriend(long id, long friendId);

    public List<User> getAllFriends(long id);

    public List<User> getCommonFriends(long id, long friendId);
}
