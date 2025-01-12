package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@Builder
public class User {
    Long id;
    String name;
    LocalDate birthday;

    @NotBlank
    @Email
    String email;

    @NotBlank
    @Length(min = 2, max = 20)
    String login;
}