package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class User {

    private Integer id;
    @NotBlank
    @Email(message = "Задан неверный адрес электронной почты")
    private String email;
    @NotBlank (message = "Логин не может быть пустым")
    @Pattern(regexp = "[\\S]*[\\S]", message = "Логин не должен содердать пробел")
    private String login;
    private String name;
    @Past(message = "Дата рождения не может быть в будущем.")
    private LocalDate birthday;
}
