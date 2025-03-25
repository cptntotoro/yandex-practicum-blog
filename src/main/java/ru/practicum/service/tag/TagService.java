package ru.practicum.service.tag;

import ru.practicum.model.tag.Tag;
import ru.practicum.service.BaseService;

import java.util.List;
import java.util.UUID;

/**
 * Сервис управления тегами
 */
public interface TagService extends BaseService<Tag> {
    /**
     * Получить все теги
     *
     * @return Список тегов
     */
    List<Tag> getAll();

    /**
     * Получить теги по идентификатору поста
     *
     * @param postUuid Идентификатор поста
     * @return Список тегов
     */
    List<Tag> getAllBy(UUID postUuid);

    /**
     * Удалить все теги по идентификатору поста
     * @param postUuid Идентификатор поста
     */
    void deleteAllBy(UUID postUuid);

    /**
     * Обновить теги поста
     * @param postUuid Идентификатор поста
     * @param postTagUuids Список идентификаторов поста
     */
    void batchUpdatePostTags(UUID postUuid, List<UUID> postTagUuids);
}
