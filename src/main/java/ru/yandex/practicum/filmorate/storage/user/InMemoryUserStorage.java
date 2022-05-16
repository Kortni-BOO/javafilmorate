package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Long, User> users = new HashMap<>();
    private long id = 0;

    private long generateId() {
        return ++id;
    }

    //создание пользователя
    @Override
    public User create(User user) {
        checkData(user);
        checkEmail(user);
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    //обновление пользователя;
    @Override
    public User update(User user) {
        checkData(user);
        if(users.get(user.getId()) != null) {
            users.put(user.getId(), user);
        }
        return users.get(user.getId());
    }

    //получение списка всех пользователей
    @Override
    public List<User> findAll() {
        List<User> usersList = new ArrayList<>(users.values());
        return usersList;
    }

    //получить пользователя по id
    @Override
    public Optional<User> getById(long id) {
        //Optional<User> optUser = Optional.ofNullable(users.get(id));

        if(id < 0) {
            throw new UserNotFoundException("Пользователь № %d не найден");
        }

       return Optional.ofNullable(users.get(id));
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

    public void checkEmail(User user) {
        ArrayList<String> emails = new ArrayList<>();
        for(User users: users.values()) {
            users.getEmail();
            emails.add(users.getEmail());
        }
        if(emails.contains(user.getEmail())) {
            throw new ValidationException("Пользователь с электронной почтой уже зарегистрирован.");
        }

    }
}
