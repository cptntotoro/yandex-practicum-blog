package ru.practicum.service.comment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.dao.comment.CommentDao;
import ru.practicum.exception.comment.InvalidCommentException;
import ru.practicum.model.comment.Comment;
import ru.practicum.repository.comment.CommentRepository;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class CommentServiceTest {
    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Test
    void save_shouldReturnComment() {
        Comment commentToSave = new Comment.Builder()
                .content("Текст")
                .postUuid(UUID.fromString("3c601d4b-f955-40e8-b6cd-c16490054195"))
                .build();

        UUID commentUuid = UUID.randomUUID();
        CommentDao fullComment = new CommentDao.Builder()
                .content("Текст")
                .postUuid(UUID.fromString("3c601d4b-f955-40e8-b6cd-c16490054195"))
                .uuid(commentUuid)
                .dateTime(LocalDateTime.now())
                .build();

        Mockito.when(commentRepository.save(any()))
                .thenReturn(commentUuid);
        Mockito.when(commentRepository.get(commentUuid))
                .thenReturn(fullComment);

        Comment comment = commentService.save(commentToSave);

        assertNotNull(comment);
        verify(commentRepository, atMostOnce()).save(any());
        verify(commentRepository, atMostOnce()).get(any());
    }

    @Test
    void save_shouldThrowInvalidCommentException() {
        Comment commentToSave = new Comment.Builder()
                .content("")
                .postUuid(UUID.fromString("3c601d4b-f955-40e8-b6cd-c16490054195"))
                .build();

        assertThrows(InvalidCommentException.class, () -> commentService.save(commentToSave));
    }

    @Test
    void update_shouldUpdateComment() {
        UUID commentUuid = UUID.randomUUID();

        Comment commentToUpdate = new Comment.Builder()
                .uuid(commentUuid)
                .content("Текст")
                .postUuid(UUID.fromString("3c601d4b-f955-40e8-b6cd-c16490054195"))
                .build();

        doNothing().when(commentRepository).update(any());

        assertDoesNotThrow(() -> commentService.update(commentToUpdate));

        verify(commentRepository, atMostOnce()).update(any());
    }

    @Test
    void delete_shouldCallRepository() {
        UUID commentUuid = UUID.randomUUID();
        doNothing().when(commentRepository).deleteBy(any());

        assertDoesNotThrow(() -> commentService.delete(commentUuid));

        verify(commentRepository, atMostOnce()).deleteBy(any());
    }
}