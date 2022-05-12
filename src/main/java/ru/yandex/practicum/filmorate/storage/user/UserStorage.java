package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    //создание пользователя
    public User create(@RequestBody User user);

    //обновление пользователя;
    public User update(@RequestBody User user);

    //получение списка всех пользователей
    public List<User> findAll();

    public void checkData(User user);
}
