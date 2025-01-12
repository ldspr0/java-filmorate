package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
public class Film {
    Long id;
    LocalDate releaseDate;

    @NotBlank
    String name;

    @Length(max = 200)
    String description;

    @Positive
    Integer duration;
}
