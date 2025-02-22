package ru.practicum.service.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.comment.InvalidCommentException;
import ru.practicum.mapper.comment.CommentDaoMapper;
import ru.practicum.model.comment.Comment;
import ru.practicum.repository.comment.CommentRepository;

import java.util.List;
import java.util.UUID;

/**
 * Сервис управления комментариями
 */
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> getAllBy(UUID postUuid) {
        return getAllCommentsByPostUuid(postUuid);
    }

    @Override
    @Transactional
    public Comment save(Comment comment) {
        validateComment(comment);
        UUID commentUuid = commentRepository.save(CommentDaoMapper.commentToCommentDao(comment));
        return getCommentByUuid(commentUuid);
    }

    @Override
    public void update(Comment comment) {
        validateComment(comment);
        commentRepository.update(CommentDaoMapper.commentToCommentDao(comment));
    }

    @Override
    public void delete(UUID commentUuid) {
        commentRepository.deleteBy(commentUuid);
    }

    /**
     * Получает все комментарии по UUID поста.
     *
     * @param postUuid UUID поста
     * @return Коллекция комментариев
     */
    private List<Comment> getAllCommentsByPostUuid(UUID postUuid) {
        return CommentDaoMapper.commentDaoListToCommentList(commentRepository.getAllBy(postUuid));
    }

    /**
     * Получает комментарий по UUID.
     *
     * @param commentUuid UUID комментария
     * @return Комментарий
     */
    private Comment getCommentByUuid(UUID commentUuid) {
        return CommentDaoMapper.commentDaoToComment(commentRepository.get(commentUuid));
    }

    /**
     * Валидирует комментарий.
     *
     * @param comment Комментарий
     * @throws InvalidCommentException если комментарий невалиден
     */
    private void validateComment(Comment comment) {
        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            throw new InvalidCommentException("Содержание комментария не может быть пустым");
        }
    }
}