package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class MpaService implements ServiceLayer<Mpa> {
    private final MpaDbStorage mpaDbStorage;

    public Collection<Mpa> getAll() {
        return mpaDbStorage.getAll();
    }

    @Override
    public Mpa create(Mpa entity) {
        return mpaDbStorage.create(entity);
    }

    @Override
    public Mpa read(long id) {
        return mpaDbStorage.read(id);
    }

    @Override
    public Mpa update(Mpa entity) {
        return mpaDbStorage.update(entity);
    }

    @Override
    public boolean delete(long id) {
        return mpaDbStorage.delete(id);
    }
}
