package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;

public interface Storage<T> {
    Collection<T> getAll();

    T create(T entity);

    T read(long id);

    T update(T entity);

    boolean delete(long id);
}
