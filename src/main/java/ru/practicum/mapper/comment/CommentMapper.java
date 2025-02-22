package ru.practicum.mapper.comment;

import ru.practicum.dto.comment.CommentAddDto;
import ru.practicum.dto.comment.CommentUpdateDto;
import ru.practicum.dto.comment.CommentViewDto;
import ru.practicum.model.comment.Comment;

import java.util.List;
import java.util.UUID;

/**
 * Маппер комментариев для контроллера
 */
public class CommentMapper {

    /**
     * Смаппить DTO комментарий
     * @param comment Комментарий
     * @return DTO комментарий
     */
    public static CommentViewDto commentToCommentViewDto(Comment comment) {
        return new CommentViewDto.Builder()
                .uuid(comment.getUuid())
                .content(comment.getContent())
                .dateTime(comment.getDateTime())
                .build();
    }

    /**
     * Смаппить список DTO комментариев
     * @param comments Список комментариев
     * @return Список DTO комментариев
     */
    public static List<CommentViewDto> commentsToCommentViewDtoList(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::commentToCommentViewDto)
                .toList();
    }

    /**
     * Смаппить комментарий для добавления в комментарий
     * @param postUuid Идентификатор поста
     * @param commentAddDto Комментарий для добавления
     * @return Комментарий
     */
    public static Comment commentAddDtoToComment(UUID postUuid, CommentAddDto commentAddDto) {
        return new Comment.Builder()
                .postUuid(postUuid)
                .content(commentAddDto.getContent())
                .build();
    }

    /**
     * Смаппить комментарий для добавления в комментарий
     * @param postUuid Идентификатор поста
     * @param commentUuid Идентификатор комментария
     * @param commentUpdateDto Комментарий для обновления
     * @return Комментарий
     */
    public static Comment commentUpdateDtoToComment(UUID postUuid, UUID commentUuid, CommentUpdateDto commentUpdateDto) {
        return new Comment.Builder()
                .uuid(commentUuid)
                .postUuid(postUuid)
                .content(commentUpdateDto.getContent())
                .build();
    }
}
