package ru.practicum.repository.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.practicum.config.DataSourceConfig;
import ru.practicum.config.WebConfig;
import ru.practicum.dao.comment.CommentDao;
import ru.practicum.exception.comment.CommentNotFoundException;
import ru.practicum.exception.post.PostNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = {DataSourceConfig.class, WebConfig.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@WebAppConfiguration
class CommentRepositoryTest {
    private static final String COMMENT_1 = "Комментарий 1";
    private static final String COMMENT_2 = "Комментарий 2";
    private static final String NEW_COMMENT = "Новый комментарий";
    private static final String UPDATED_COMMENT = "Обновленный комментарий";
    private static final String NON_EXISTENT_POST_COMMENT = "Комментарий к несуществующему посту";
    private static final String NON_EXISTENT_COMMENT = "Несуществующий комментарий";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private CommentRepository commentRepository;

    private UUID comment1uuid;
    private UUID comment2uuid;
    private UUID post1uuid;
    private UUID post2uuid;
    private UUID nonExistentUuid;

    @BeforeEach
    void setUp() {
        comment1uuid = UUID.randomUUID();
        comment2uuid = UUID.randomUUID();
        post1uuid = UUID.randomUUID();
        post2uuid = UUID.randomUUID();
        nonExistentUuid = UUID.randomUUID();

        clearDatabase();
        initializeTestData();
    }

    private void clearDatabase() {
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM posts");
    }

    private void initializeTestData() {
        insertPost(post1uuid, "Пост 1", "Контент поста 1");
        insertPost(post2uuid, "Пост 2", "Контент поста 2");

        insertComment(comment1uuid, post1uuid, COMMENT_1, LocalDateTime.now());
        insertComment(comment2uuid, post1uuid, COMMENT_2, LocalDateTime.now().minusHours(1));
    }

    private void insertPost(UUID postUuid, String title, String content) {
        jdbcTemplate.update(
                "INSERT INTO posts (post_uuid, title, text_content, date_time) VALUES (?, ?, ?, ?)",
                postUuid, title, content, LocalDateTime.now()
        );
    }

    private void insertComment(UUID commentUuid, UUID postUuid, String content, LocalDateTime dateTime) {
        jdbcTemplate.update(
                "INSERT INTO comments (comment_uuid, post_uuid, text_content, date_time) VALUES (?, ?, ?, ?)",
                commentUuid, postUuid, content, dateTime
        );
    }

    @Test
    void get_shouldReturnCommentWhenExists() {
        CommentDao comment = commentRepository.get(comment1uuid);

        assertAll(
                () -> assertNotNull(comment),
                () -> assertEquals(comment1uuid, comment.getUuid()),
                () -> assertEquals(post1uuid, comment.getPostUuid()),
                () -> assertEquals(COMMENT_1, comment.getContent()),
                () -> assertNotNull(comment.getDateTime())
        );
    }

    @Test
    void get_shouldThrowExceptionWhenCommentNotExists() {
        assertThrows(CommentNotFoundException.class, () -> commentRepository.get(nonExistentUuid));
    }

    @Test
    void save_shouldSaveNewCommentAndReturnUuid() {
        CommentDao newComment = buildComment(post2uuid, NEW_COMMENT);

        UUID savedUuid = commentRepository.save(newComment);
        CommentDao savedComment = commentRepository.get(savedUuid);

        assertAll(
                () -> assertNotNull(savedUuid),
                () -> assertEquals(NEW_COMMENT, savedComment.getContent()),
                () -> assertEquals(post2uuid, savedComment.getPostUuid())
        );
    }

    @Test
    void save_shouldThrowPostNotFoundExceptionForNonExistingPost() {
        CommentDao comment = buildComment(nonExistentUuid, NON_EXISTENT_POST_COMMENT);

        assertThrows(PostNotFoundException.class, () -> commentRepository.save(comment));
    }

    @Test
    void update_shouldUpdateExistingComment() {
        CommentDao updatedComment = buildComment(comment1uuid, post1uuid, UPDATED_COMMENT);

        commentRepository.update(updatedComment);
        CommentDao result = commentRepository.get(comment1uuid);

        assertEquals(UPDATED_COMMENT, result.getContent());
    }

    @Test
    void update_shouldThrowExceptionWhenCommentNotExists() {
        CommentDao nonExistentComment = buildComment(nonExistentUuid, post1uuid, NON_EXISTENT_COMMENT);

        assertThrows(CommentNotFoundException.class, () -> commentRepository.update(nonExistentComment));
    }

    @Test
    void deleteBy_shouldDeleteExistingComment() {
        commentRepository.deleteBy(comment1uuid);

        assertThrows(CommentNotFoundException.class, () -> commentRepository.get(comment1uuid));
    }

    @Test
    void deleteBy_shouldThrowExceptionWhenCommentNotExists() {
        assertThrows(CommentNotFoundException.class, () -> commentRepository.deleteBy(nonExistentUuid));
    }

    @Test
    void getAllBy_shouldReturnCommentsForPost() {
        List<CommentDao> comments = commentRepository.getAllBy(post1uuid);

        assertAll(
                () -> assertEquals(2, comments.size()),
                () -> assertTrue(comments.stream().anyMatch(c -> c.getUuid().equals(comment1uuid))),
                () -> assertTrue(comments.stream().anyMatch(c -> c.getUuid().equals(comment2uuid)))
        );
    }

    @Test
    void getAllBy_shouldReturnCommentsInCorrectOrder() {
        List<CommentDao> comments = commentRepository.getAllBy(post1uuid);

        assertTrue(comments.get(0).getDateTime().isAfter(comments.get(1).getDateTime()));
    }

    @Test
    void getAllBy_shouldReturnEmptyListForPostWithoutComments() {
        List<CommentDao> comments = commentRepository.getAllBy(post2uuid);

        assertTrue(comments.isEmpty());
    }

    private CommentDao buildComment(UUID postUuid, String content) {
        return new CommentDao.Builder()
                .postUuid(postUuid)
                .content(content)
                .build();
    }

    private CommentDao buildComment(UUID commentUuid, UUID postUuid, String content) {
        return new CommentDao.Builder()
                .uuid(commentUuid)
                .postUuid(postUuid)
                .content(content)
                .build();
    }
}