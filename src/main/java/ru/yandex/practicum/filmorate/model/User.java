package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {
    private Long id;
    private String name;

    @PastOrPresent
    private LocalDate birthday;

    @NotBlank
    @Email
    private String email;

    @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$",
            message = "username must be of 2 to 20 length with no special characters")
    private String login;
}