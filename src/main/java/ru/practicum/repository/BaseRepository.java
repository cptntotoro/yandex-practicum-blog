package ru.practicum.repository;

import java.util.List;
import java.util.UUID;

public interface BaseRepository<T> {
    /**
     * Получить сущность по идентификатору
     * @param uuid Идентификатор
     * @return Сущность
     */
    T get(UUID uuid);

    /**
     * Сохранить сущность
     * @param t Сущность
     * @return Сущность
     */
    UUID save(T t);

    /**
     * Обновить сущность
     * @param t Сущность
     */
    void update(T t);

    /**
     * Удалить сущность по идентификатору
     * @param uuid Идентификатор
     */
    void deleteBy(UUID uuid);

    /**
     * Получить все сущности по идентификатору другой сущности
     * @param uuid Идентификатор сущности
     * @return Список сущностей
     */
    List<T> getAllBy(UUID uuid);
}
