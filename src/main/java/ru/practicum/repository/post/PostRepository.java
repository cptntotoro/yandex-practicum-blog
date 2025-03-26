package ru.practicum.repository.post;

import ru.practicum.dao.post.PostDao;
import ru.practicum.repository.BaseRepository;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий постов
 */
public interface PostRepository extends BaseRepository<PostDao> {

    /**
     * Увеличить число лайков поста на единицу
     *
     * @param postUUID Идентификатор поста
     */
    void setLike(UUID postUUID);

    /**
     * Получить страницу постов
     *
     * @param page Страница
     * @param size Число постов на странице
     * @return Список постов
     */
    List<PostDao> getPage(int page, int size);

    /**
     * Получить общее число постов
     *
     * @return Число постов
     */
    long getTotal();

    /**
     * Проверить существование поста по идентификатору
     *
     * @param postUuid Идентификатор поста
     * @return Да/Нет
     */
    boolean isExist(UUID postUuid);
}
