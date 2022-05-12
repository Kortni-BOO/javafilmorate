package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    //добавление в друзья
    public void addFriend(User user, User userFriend) {
        if(user.getFriends().contains(userFriend.getId())) {
            //выбросим исключение
        }
        user.addFriends(userFriend.getId());
        userFriend.addFriends(user.getId());
    }
    //удаление из друзей
    public void deleteFriend(User user, User userFriend) {
        user.getFriends().remove(userFriend.getId());
        userFriend.getFriends().remove(user.getId());
    }
    //вывод списка общих друзей
    public Set<Long> getCommonFriends(User user, User userFriend) {
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
