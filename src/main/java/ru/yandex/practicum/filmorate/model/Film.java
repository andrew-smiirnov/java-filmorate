package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Data
public class Film {
    private Long id;
    @NotBlank (message = "Название не может быть пустым.")
    private String name;
    @Size(min=1, max=200, message = "Максимальная длина описания — 200 символов")
    private String description;
    private LocalDate releaseDate;
    @Positive (message = "Продолжительность фильма не может быть отрицательной")
    private Integer duration;
    private Set<Long> likes = new TreeSet<>();
    private Integer rate;
}
