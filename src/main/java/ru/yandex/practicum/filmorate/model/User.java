package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Data
public class User {

    private Long id;
    @NotBlank
    @Email(message = "Задан неверный адрес электронной почты")
    private String email;
    @NotBlank (message = "Логин не может быть пустым")
    @Pattern(regexp = "[\\S]*[\\S]", message = "Логин не должен содердать пробел")
    private String login;
    private String name;
    @Past(message = "Дата рождения не может быть в будущем.")
    private LocalDate birthday;
    private Set<Long> friends = new TreeSet<>();
}
