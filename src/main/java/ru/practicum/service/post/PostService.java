package ru.practicum.service.post;

import ru.practicum.model.post.Post;
import ru.practicum.model.post.PostPreview;
import ru.practicum.service.BaseService;

import java.util.List;
import java.util.UUID;

/**
 * Сервис управления постами
 */
public interface PostService extends BaseService<Post> {

    /**
     * Получить все посты по тегу
     * @param tagUuid Идентификатор тега
     * @return Список постов
     */
    List<PostPreview> getAllByTag(UUID tagUuid);

    /**
     * Поставить лайк посту по идентификатору
     * @param postUuid Идентификатор постав
     */
    void setLike(UUID postUuid);

    /**
     * Обновить пост
     * @param post Пост
     * @return Пост
     */
    Post update(Post post);

    /**
     * Получить пост по идентификатору
     * @param uuid Идентификатор
     * @return Пост
     */
    Post get(UUID uuid);

    /**
     * Получить страницу постов
     * @param page - номер страницы
     * @param size - размер выдачи
     * @return - список постов для страницы
     */
    List<PostPreview> getPage(int page, int size);

    /**
     * Получить число всех постов
     * @return Число всех постов
     */
    long getTotal();
}
