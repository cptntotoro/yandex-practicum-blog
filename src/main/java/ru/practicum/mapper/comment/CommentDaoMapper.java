package ru.practicum.mapper.comment;

import ru.practicum.dao.comment.CommentDao;
import ru.practicum.model.comment.Comment;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Маппер комментариев для БД
 */
public class CommentDaoMapper {
    /**
     * Смаппить список комментариев для БД в список комметариев
     *
     * @param commentList Список комментариев для БД
     * @return Список комметариев
     */
    public static List<Comment> commentDaoListToCommentList(List<CommentDao> commentList) {
        return commentList.stream()
                .map(CommentDaoMapper::commentDaoToComment)
                .collect(Collectors.toList());
    }

    /**
     * Смаппить комментарий для БД в комметарий
     *
     * @param commentDao Комментарий для БД
     * @return Комментарий
     */
    public static Comment commentDaoToComment(CommentDao commentDao) {
        return new Comment.Builder()
                .uuid(commentDao.getUuid())
                .postUuid(commentDao.getPostUuid())
                .content(commentDao.getContent())
                .dateTime(commentDao.getDateTime())
                .build();
    }

    /**
     * Смаппить комментарий в комметарий для БД
     *
     * @param comment Комментарий
     * @return Комментарий для БД
     */
    public static CommentDao commentToCommentDao(Comment comment) {
        return new CommentDao.Builder()
                .uuid(comment.getUuid())
                .postUuid(comment.getPostUuid())
                .content(comment.getContent())
                .dateTime(comment.getDateTime())
                .build();
    }
}
