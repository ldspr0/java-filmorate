package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@Builder
public class Film {
    private Long id;
    private LocalDate releaseDate;

    @NotBlank
    private String name;

    @Length(max = 200)
    private String description;

    @Positive
    private Integer duration;
}
