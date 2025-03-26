package ru.practicum.service.comment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.dao.comment.CommentDao;
import ru.practicum.exception.comment.InvalidCommentException;
import ru.practicum.exception.post.PostNotFoundException;
import ru.practicum.model.comment.Comment;
import ru.practicum.repository.comment.CommentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class CommentServiceTest {
    private static final String COMMENT_TEXT = "Текст комментария";
    private static final String COMMENT_1 = "Comment 1";
    private static final String COMMENT_2 = "Comment 2";
    private static final String EMPTY_COMMENT = "";
    private static final String POST_NOT_FOUND_MSG = "Post not found";

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    private final UUID postUuid = UUID.fromString("3c601d4b-f955-40e8-b6cd-c16490054195");
    private final UUID commentUuid1 = UUID.randomUUID();
    private final UUID commentUuid2 = UUID.randomUUID();

    @Test
    void save_shouldReturnComment() {
        Comment commentToSave = buildComment(COMMENT_TEXT, postUuid);
        CommentDao fullComment = buildCommentDao(commentUuid1, COMMENT_TEXT, postUuid);

        when(commentRepository.save(any())).thenReturn(commentUuid1);
        when(commentRepository.get(commentUuid1)).thenReturn(fullComment);

        Comment result = commentService.save(commentToSave);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(COMMENT_TEXT, result.getContent()),
                () -> assertEquals(postUuid, result.getPostUuid())
        );

        verify(commentRepository).save(any());
        verify(commentRepository).get(commentUuid1);
    }

    @Test
    void save_shouldThrowInvalidCommentException() {
        Comment invalidComment = buildComment(EMPTY_COMMENT, postUuid);

        assertThrows(InvalidCommentException.class, () -> commentService.save(invalidComment));
        verifyNoInteractions(commentRepository);
    }

    @Test
    void update_shouldUpdateComment() {
        Comment commentToUpdate = buildComment(commentUuid1, COMMENT_TEXT, postUuid);

        assertDoesNotThrow(() -> commentService.update(commentToUpdate));
        verify(commentRepository).update(any());
    }

    @Test
    void getAllBy_shouldReturnCommentsForPost() {
        CommentDao commentDao1 = buildCommentDao(commentUuid1, COMMENT_1, postUuid);
        CommentDao commentDao2 = buildCommentDao(commentUuid2, COMMENT_2, postUuid, LocalDateTime.now().minusHours(1));

        when(commentRepository.getAllBy(postUuid)).thenReturn(List.of(commentDao1, commentDao2));

        List<Comment> result = commentService.getAllBy(postUuid);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertEquals(commentUuid1, result.getFirst().getUuid()),
                () -> assertEquals(COMMENT_1, result.getFirst().getContent()),
                () -> assertEquals(commentUuid2, result.get(1).getUuid()),
                () -> assertEquals(COMMENT_2, result.get(1).getContent())
        );

        verify(commentRepository).getAllBy(postUuid);
    }

    @Test
    void getAllBy_shouldThrowPostNotFoundException() {
        when(commentRepository.getAllBy(postUuid))
                .thenThrow(new PostNotFoundException(POST_NOT_FOUND_MSG));

        assertThrows(PostNotFoundException.class, () -> commentService.getAllBy(postUuid));
        verify(commentRepository).getAllBy(postUuid);
    }

    @Test
    void delete_shouldCallRepository() {
        assertDoesNotThrow(() -> commentService.delete(commentUuid1));
        verify(commentRepository).deleteBy(commentUuid1);
    }

    private Comment buildComment(String content, UUID postUuid) {
        return new Comment.Builder()
                .content(content)
                .postUuid(postUuid)
                .build();
    }

    private Comment buildComment(UUID uuid, String content, UUID postUuid) {
        return new Comment.Builder()
                .uuid(uuid)
                .content(content)
                .postUuid(postUuid)
                .build();
    }

    private CommentDao buildCommentDao(UUID uuid, String content, UUID postUuid) {
        return buildCommentDao(uuid, content, postUuid, LocalDateTime.now());
    }

    private CommentDao buildCommentDao(UUID uuid, String content, UUID postUuid, LocalDateTime dateTime) {
        return new CommentDao.Builder()
                .uuid(uuid)
                .content(content)
                .postUuid(postUuid)
                .dateTime(dateTime)
                .build();
    }
}