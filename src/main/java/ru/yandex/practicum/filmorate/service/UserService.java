package ru.yandex.practicum.filmorate.service;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Service
//@RequiredArgsConstructor
public class UserService {

    private UserStorage userStorage;

    //@Autowired
    public UserService(@Qualifier("userDbStorage")UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    //создание пользователя
    public User create(User user) {
        checkData(user);
        checkEmail(user);
        return userStorage.create(user);
    }

    //обновление пользователя;
    public User update(User user) {
        checkData(user);

        return userStorage.update(user);
    }

    //получение списка всех пользователей
    public List<User> findAll() {
        return userStorage.findAll();
    }

    //получение пользователя по id
    public User getById(@NonNull long id) {
        User user = userStorage.getById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден."));
        return user;
    }

    //добавление в друзья
    public void addFriend(long id, long friendId) {
        userStorage.addFriend(id, friendId);
    }

    //удаление из друзей
    public void deleteFriend(long id, long friendId) {
        userStorage.deleteFriend(id, friendId);
    }

    //вернуть список друзей
    public List<User> getAllFriends(long id) {
        return userStorage.getAllFriends(id);
    }

    //вывод списка общих друзей
    public List<User> getCommonFriends(long id, long friendId) {
        return userStorage.getCommonFriends(id, friendId);
    }

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

    public void checkEmail(User user) {
        ArrayList<String> emails = new ArrayList<>();
        List<User> users = userStorage.findAll();
        for(User userId: users) {
            userId.getEmail();
            emails.add(userId.getEmail());
        }
        if(emails.contains(user.getEmail())) {
            throw new ValidationException("Пользователь с электронной почтой уже зарегистрирован.");
        }

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
