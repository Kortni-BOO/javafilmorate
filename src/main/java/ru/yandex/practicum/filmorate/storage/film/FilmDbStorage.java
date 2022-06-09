package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpaa;
import ru.yandex.practicum.filmorate.service.RateSizeComparator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    private RateSizeComparator comparator = new RateSizeComparator();

    @Override
    public Film create(Film film) {
        String sqlQuery = "INSERT INTO film (name, description, release_date, duration, mpaa_id) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
                stmt.setString(1, film.getName());
                stmt.setString(2, film.getDescription());
                stmt.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
                stmt.setLong(4, film.getDuration());
                stmt.setInt(5, film.getMpaa().getId());
                return stmt;
            }, keyHolder);
        film.setId(
                keyHolder.getKey().intValue()
        );
        return film;
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE film SET " +
                "name = ?, description = ?, release_date = ?, duration = ?, mpaa_id = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpaa().getId(),
                film.getId());
        return film;
    }

    @Override
    public List<Film> findAll() {
        String sqlQuery = "SELECT id, name, description, release_date, duration, mpaa_id FROM film";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Optional<Film> getById(long id) {
        String sqlQuery = "SELECT id, name, description, release_date, duration, mpaa_id FROM film WHERE id = ?";
        Film film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
        return Optional.of(film);
    }

    public void like(long id, long userId) {
        String sqlQuery = "INSERT INTO `like` (user_id, film_id) VALUES (?,?)";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    public void deleteLike(long id, long userId) {
        String sqlQuery = "DELETE FROM `like` WHERE user_id = ? AND film_id = ?";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    public List<Film> getHitMovie(int count) {
        String sqlQuery = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.mpaa_id " +
                "FROM film AS f " +
                "LEFT JOIN `like` AS l ON f.id = l.film_id " +
                "GROUP BY f.id " +
                "ORDER BY COUNT(l.user_id) " +
                "LIMIT ?";
            return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }

    private Film mapRowToFilm(ResultSet rowSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(rowSet.getInt("id"))
                .name(rowSet.getString("name"))
                .description(rowSet.getString("description"))
                .releaseDate(rowSet.getDate("release_date").toLocalDate())
                .duration(rowSet.getInt("duration"))
                .mpaa(Mpaa.builder()
                        .id(rowSet.getInt("id"))
                        .name(rowSet.getString("name"))
                        .build())
                .build();
    }
}