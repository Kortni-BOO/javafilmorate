package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;

    @Test
    public void testFindUserById() {
        Optional<User> userOptional = userStorage.getById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testFindAllUsers() {
        List<User> users = userStorage.findAll();

        assertThat(users).hasSize(3);
    }

    @Test
    public void testUpdateUser() {
        Optional<User> userOptional = userStorage.getById(3);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "romashka")
                );

        userStorage.update(User.builder()
                .id(3)
                .email("rr@email.ru")
                .name("Rr Romashka")
                .birthday(LocalDate.of(1994, 02, 06))
                .login("rom_pom")
                .build());

        Optional<User> userOptionalUpdated = userStorage.getById(3);

        assertThat(userOptionalUpdated)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "rom_pom")
                );
    }

    @Test
    public void testAddFriend() {
        List<User> friends = userStorage.getAllFriends(1);
        assertThat(friends).hasSize(2);
    }

    @Test
    public void testGetCommonFriends() {
        List<User> friends = userStorage.getCommonFriends(1, 2);
        assertThat(friends).hasSize(1);
    }

    @Test
    public void testFindFilmById() {
        Optional<Film> filmOptional = filmStorage.getById(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testFindAllFilms() {
        List<Film> films = filmStorage.findAll();

        assertThat(films).hasSize(2);
    }

}