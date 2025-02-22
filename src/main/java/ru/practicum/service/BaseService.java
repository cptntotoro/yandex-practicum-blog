package ru.practicum.service;

import java.util.UUID;

public interface BaseService<T> {

    /**
     * Сохранить сущность
     * @param t Сущность
     * @return Сущность
     */
    T save(T t);

    /**
     * Удалить сущность по идентификатору
     * @param uuid Идентификатор
     */
    void delete(UUID uuid);
}
