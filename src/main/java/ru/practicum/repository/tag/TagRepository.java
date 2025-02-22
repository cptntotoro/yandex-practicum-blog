package ru.practicum.repository.tag;

import ru.practicum.dao.tag.TagDao;
import ru.practicum.repository.BaseRepository;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий тегов
 */
public interface TagRepository extends BaseRepository<TagDao> {

    /**
     * Удалить все теги по идентификатору поста
     * @param postUuid Идентификатор поста
     */
    void deleteAllBy(UUID postUuid);

    /**
     * Получить все теги
     * @return Список тегов
     */
    List<TagDao> getAll();

    /**
     * Обновить теги поста
     */
    void batchUpdatePostTags(UUID postUuid, List<UUID> postTagUuids);
}
