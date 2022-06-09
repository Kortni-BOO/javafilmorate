package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@Builder
public class User {
    private long id;//целочисленный идентификатор;
    private String email;//электронная почта;
    @Pattern(regexp = "^\\S*$")
    private String login;//логин пользователя;
    private String name;//имя для отображения;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;//дата рождения
    @JsonIgnore
    private Set<Long> friends = new HashSet<>();//список друзей


}
