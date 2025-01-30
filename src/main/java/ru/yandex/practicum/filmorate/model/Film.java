package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class Film {
    private Long id;
    private LocalDate releaseDate;
    private Set<Long> likes;

    @NotBlank
    private String name;

    @Length(max = 200)
    private String description;

    @Positive
    private Integer duration;
}
