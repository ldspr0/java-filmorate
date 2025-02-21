package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class GenreService implements ServiceLayer<Genre> {
    private final GenreDbStorage genreStorage;

    public Collection<Genre> getAll() {
        return genreStorage.getAll();
    }

    @Override
    public Genre create(Genre entity) {
        return genreStorage.create(entity);
    }

    @Override
    public Genre read(long id) {
        return genreStorage.read(id);
    }

    @Override
    public Genre update(Genre entity) {
        return genreStorage.update(entity);
    }

    @Override
    public boolean delete(long id) {
        return genreStorage.delete(id);
    }
}
