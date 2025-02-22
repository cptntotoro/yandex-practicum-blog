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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitConfig(classes = {DataSourceConfig.class, WebConfig.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@WebAppConfiguration
public class CommentRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CommentRepository commentRepository;

    private static final UUID comment1uuid = UUID.randomUUID();
    private static final UUID comment2uuid = UUID.randomUUID();
    private static final UUID post1uuid = UUID.randomUUID();
    private static final UUID post2uuid = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM posts");

        String sqlAddPost = "INSERT INTO posts (post_uuid, title, image_url, text_content, date_time) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlAddPost, post1uuid, "Описание поста1", "https://upload.wikimedia.org/wikipedia/commons/4/4d/Cat_November_2010-1a.jpg", "Контент поста1", LocalDateTime.now());
        jdbcTemplate.update(sqlAddPost, post2uuid, "Описание поста2", null, "Контент поста 2", LocalDateTime.now());

        String sqlAddComments = "INSERT INTO comments (comment_uuid, post_uuid, text_content, date_time) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sqlAddComments, comment1uuid, post1uuid, "Контент комментария 1", LocalDateTime.now());
        jdbcTemplate.update(sqlAddComments, comment2uuid, post1uuid, "Контент комментария 2", LocalDateTime.now());
    }

    @Test
    void getAll_shouldReturnComments() {
        List<CommentDao> comments = commentRepository.getAllBy(post1uuid).stream().toList();

        assertNotNull(comments);
        assertEquals(2, comments.size());
    }

    @Test
    void get_shouldReturnComment() {
        CommentDao comment = commentRepository.get(comment1uuid);
        assertNotNull(comment);
    }

    @Test
    void save_shouldAddComment() {
        CommentDao comment = new CommentDao.Builder()
                .postUuid(post2uuid)
                .content("Контент комментария 3")
                .build();

        UUID savedCommentUuid = commentRepository.save(comment);

        assertNotNull(savedCommentUuid);
    }

    @Test
    void update_shouldUpdateComment() {
        CommentDao comment = new CommentDao.Builder()
                .uuid(comment2uuid)
                .postUuid(post1uuid)
                .content("Новый контент комментария 2")
                .build();

        commentRepository.update(comment);

        CommentDao updatedComment = commentRepository.get(comment2uuid);

        assertNotNull(updatedComment);
        assertNotNull(updatedComment.getUuid());
        assertEquals("Новый контент комментария 2", updatedComment.getContent());
    }

    @Test
    void getAllByPostUuid_shouldReturnCommentsByPostUuid() {
        List<CommentDao> comments = commentRepository.getAllBy(post1uuid).stream().toList();

        assertNotNull(comments);
        assertNotNull(comments.getFirst().getUuid());
        assertNotNull(comments.getFirst().getPostUuid());
        assertNotNull(comments.getFirst().getContent());
        assertEquals(2, comments.size());
    }
}
