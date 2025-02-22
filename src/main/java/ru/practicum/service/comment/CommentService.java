package ru.practicum.service.comment;

import ru.practicum.model.comment.Comment;
import ru.practicum.service.BaseService;

import java.util.List;
import java.util.UUID;

/**
 * Сервис управления комментариями
 */
public interface CommentService extends BaseService<Comment> {

    /**
     * Обновить комментарий
     *
     * @param comment Комментарий
     */
    void update(Comment comment);

    /**
     * Получить все комментарии по идентификатору поста
     *
     * @param postUuid Идентификатор поста
     * @return Список комментариев
     */
    List<Comment> getAllBy(UUID postUuid);
}
