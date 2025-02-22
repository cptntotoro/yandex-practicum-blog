package ru.practicum.repository.post;

import ru.practicum.dao.post.PostDao;
import ru.practicum.repository.BaseRepository;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends BaseRepository<PostDao> {
//    /**
//     * Получить все посты
//     *
//     * @return Список постов
//     */
//    List<PostDao> getAll();

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
}
