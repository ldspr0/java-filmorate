package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    Long id;
    LocalDate releaseDate;
    Duration duration;

    @NotBlank
    String name;

    @Length(max = 200)
    String description;
}
