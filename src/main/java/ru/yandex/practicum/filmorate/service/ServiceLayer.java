package ru.yandex.practicum.filmorate.service;

import java.util.Collection;

public interface ServiceLayer<T> {
    Collection<T> getAll();

    T create(T entity);

    T read(long id);

    T update(T entity);

    boolean delete(long id);
}
