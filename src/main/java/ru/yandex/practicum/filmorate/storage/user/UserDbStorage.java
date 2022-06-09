package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {
        String sqlQuery = "INSERT " +
                          "INTO users (email, login, name, birthday) " +
                          "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
                stmt.setString(1, user.getEmail());
                stmt.setString(2, user.getLogin());
                stmt.setString(3, user.getName());
                stmt.setDate(4, java.sql.Date.valueOf(user.getBirthday()));
                return stmt;
            }, keyHolder);
            user.setId(
                    keyHolder.getKey().intValue()
            );
        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE users SET " +
                "email = ?, login = ?, name = ?, birthday = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public List<User> findAll() {
        String sqlQuery = "SELECT * FROM users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public Optional<User> getById(long id) {
        String sqlQuery = "SELECT * FROM users WHERE id = ?";
        User user = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
        return Optional.of(user);
    }

    @Override
    public void addFriend(long id, long friendId) {
        String sqlQuery = "INSERT " +
                          "INTO friends (user_id, friend_id) VALUES ( ?,? )";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    //удаление из друзей
    public void deleteFriend(long id, long friendId) {
        String sqlQuery = "DELETE " +
                          "FROM friends " +
                          "WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    //вернуть список друзей
    public List<User> getAllFriends(long id) {
        String sqlQuery = "SELECT *" +
                          "FROM users WHERE id IN (SELECT friend_id " +
                          "FROM friends WHERE user_id = ?)";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, id);
    }

    //вывод списка общих друзей
    public List<User> getCommonFriends(long id, long friendId) {
        String sqlQuery = "SELECT * FROM users u\n" +
                "WHERE u.ID IN (SELECT friend_id\n" +
                "                    FROM friends f\n" +
                "                    WHERE f.user_id = ?)\n" +
                "AND u.id IN (SELECT friend_id\n" +
                "                  FROM friends f\n" +
                "                  WHERE f.user_id = ?)";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, id, friendId);
    }

    private User mapRowToUser(ResultSet rowSet, int rowNum) throws SQLException {
        return User.builder()
                .id(rowSet.getInt("id"))
                .email(rowSet.getString("email"))
                .login(rowSet.getString("login"))
                .name(rowSet.getString("name"))
                .birthday(rowSet.getDate("birthday").toLocalDate())
                .build();
    }
}
