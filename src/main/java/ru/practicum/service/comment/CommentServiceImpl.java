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
    /**
     * Репозиторий комментариев
     */
    private CommentRepository commentRepository;

    @Autowired
    public void setCommentRepository(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> getAllBy(UUID postUuid) {
        return CommentDaoMapper.commentDaoListToCommentList(commentRepository.getAllBy(postUuid));
    }

    @Override
    @Transactional
    public Comment save(Comment comment) {
        validateComment(comment);
        UUID commentUuid = commentRepository.save(CommentDaoMapper.commentToCommentDao(comment));
        return CommentDaoMapper.commentDaoToComment(commentRepository.get(commentUuid));
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
     * Проверить комментарий
     *
     * @param comment Комментарий
     * @throws InvalidCommentException Если комментарий невалиден
     */
    private void validateComment(Comment comment) {
        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            throw new InvalidCommentException("Содержание комментария не может быть пустым");
        }
    }
}