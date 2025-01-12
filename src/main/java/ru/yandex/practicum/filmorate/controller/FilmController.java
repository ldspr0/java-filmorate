package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/posts")
public class FilmController {
    private final Map<Long, Film> posts = new HashMap<>();


    @GetMapping
    public Collection<Post> findAll() {
        return posts.values();
    }

}
