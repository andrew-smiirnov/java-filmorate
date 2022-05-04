package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    private Integer id;
    @NotBlank (message = "Название не может быть пустым.")
    private String name;
    @Size(min=1, max=200, message = "Максимальная длина описания — 200 символов")
    private String description;
    private LocalDate releaseDate;
    private Duration duration;
}
