package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.error.ValidationException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    //создание пользователя
    public User create(User user) {
        return userStorage.create(user);
    }

    //обновление пользователя;
    public User update(User user) {
        return userStorage.update(user);
    }

    //получение списка всех пользователей
    public List<User> getAll() {
        return userStorage.findAll();
    }

    //получение пользователя по id
    public User getById(long id) {
        return userStorage.getById(id);
    }

    //добавление в друзья
    public User addFriend(long id, long friendId) {
        User user = userStorage.getById(id);
        User userFriend = userStorage.getById(friendId);
        if(user.getFriends().contains(userFriend.getId())) {
            new UserNotFoundException(String.format("Пользователь № %d не найден", id));
        }
        user.addFriends(userFriend.getId());
        userFriend.addFriends(user.getId());
        return userFriend;
    }

    //удаление из друзей
    public User deleteFriend(long id, long friendId) {
        User user = userStorage.getById(id);
        User userFriend = userStorage.getById(friendId);
        if(user.getFriends().contains(userFriend.getId())) {
            new UserNotFoundException(String.format("Пользователь № %d не найден", id));
        }
        user.getFriends().remove(userFriend.getId());
        userFriend.getFriends().remove(user.getId());
        return userFriend;
    }

    //вернуть список друзей
    public List<User> getAllFriends(long id) {
        User user = userStorage.getById(id);
        List<User> userFriends = new ArrayList<>();
        for(Long idFriend : userStorage.getById(id).getFriends()) {
            userFriends.add(userStorage.getById(idFriend));
        }
        return userFriends;
    }

    //вывод списка общих друзей
    public Set<Long> getCommonFriends(long id, long friendId) {
        User user = userStorage.getById(id);
        User userFriend = userStorage.getById(friendId);
        Set<Long> commonFriends = new HashSet<>();
        if(user.getFriends().contains(userFriend.getFriends())) {
            commonFriends.add(userFriend.getId());
        }
        return commonFriends;
    }
}

/*
Есть много способов хранить информацию о том, что два пользователя являются друзьями.
Например, можно создать свойство friends в классе пользователя,
которое будет содержать список его друзей.
Вы можете использовать такое решение или придумать своё.
Для того чтобы обеспечить уникальность значения
(мы не можем добавить одного человека в друзья дважды),
проще всего использовать для хранения Set<Long> c id друзей.
Таким же образом можно обеспечить условие «один пользователь — один лайк» для оценки фильмов.
*/
