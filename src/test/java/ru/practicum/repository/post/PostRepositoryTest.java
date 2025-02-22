package ru.practicum.repository.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.practicum.config.DataSourceConfig;
import ru.practicum.config.WebConfig;
import ru.practicum.dao.post.PostDao;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = {DataSourceConfig.class, WebConfig.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@WebAppConfiguration
public class PostRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PostRepository postRepository;

    private static final UUID post1uuid = UUID.randomUUID();
    private static final UUID post2uuid = UUID.randomUUID();
    private static final UUID post3uuid = UUID.randomUUID();
    private static final UUID tag1uuid = UUID.randomUUID();
    private static final UUID tag2uuid = UUID.randomUUID();
    private static final UUID comment1uuid = UUID.randomUUID();
    private static final UUID comment2uuid = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM tags");
        jdbcTemplate.execute("DELETE FROM posts");
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM post_tags");

        String sqlAddPost = "INSERT INTO posts (post_uuid, title, image_url, text_content, date_time) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlAddPost, post1uuid, "Название поста1", "https://upload.wikimedia.org/wikipedia/commons/4/4d/Cat_November_2010-1a.jpg", "Контент поста1", LocalDateTime.now());
        jdbcTemplate.update(sqlAddPost, post2uuid, "Название поста2", null, "Контент поста 2", LocalDateTime.now());
        jdbcTemplate.update(sqlAddPost, post3uuid, "Название поста3", null, "Контент поста 3", LocalDateTime.now());

        String sqlAddComments = "INSERT INTO comments (comment_uuid, post_uuid, text_content, date_time) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sqlAddComments, comment1uuid, post1uuid, "Контент комментария 1", LocalDateTime.now());
        jdbcTemplate.update(sqlAddComments, comment2uuid, post1uuid, "Контент комментария 2", LocalDateTime.now());

        String sqlAddTags = "INSERT INTO tags (tag_uuid, title) VALUES (?, ?)";
        jdbcTemplate.update(sqlAddTags, tag1uuid, "Тег 1");
        jdbcTemplate.update(sqlAddTags, tag2uuid, "Тег 2");

        String sqlAddTagsToPost = "INSERT INTO post_tags (pt_uuid, post_uuid, tag_uuid) VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlAddTagsToPost, UUID.randomUUID(), post1uuid, tag1uuid);
        jdbcTemplate.update(sqlAddTagsToPost, UUID.randomUUID(), post2uuid, tag2uuid);
    }

    @Test
    void getPage_shouldReturnPosts() {
        List<PostDao> posts = postRepository.getPage(1, 10).stream().toList();

        assertNotNull(posts);
        assertEquals(3, posts.size());
    }

    @Test
    void getPage_shouldReturnPostsWithPagination() {
        int page = 1; // Первая страница
        int size = 2; // 2 поста на странице

        List<PostDao> posts = postRepository.getPage(page, size).stream().toList();

        assertNotNull(posts);
        assertEquals(2, posts.size()); // Ожидаем 2 поста на первой странице
    }

    @Test
    void getPage_shouldReturnEmptyListForInvalidPage() {
        int page = 10; // Несуществующая страница
        int size = 2; // 2 поста на странице

        List<PostDao> posts = postRepository.getPage(page, size).stream().toList();

        assertNotNull(posts);
        assertTrue(posts.isEmpty());
    }

    @Test
    void getPage_shouldReturnAllPostsIfSizeIsLarge() {
        int page = 1; // Первая страница
        int size = 10; // 10 постов на странице (больше, чем общее количество постов)

        List<PostDao> posts = postRepository.getPage(page, size).stream().toList();

        assertNotNull(posts);
        assertEquals(3, posts.size()); // Ожидаем все 3 поста, так как size больше общего количества
    }

    @Test
    void get_shouldReturnPost() {
        PostDao post = postRepository.get(post1uuid);

        assertNotNull(post);
        assertNotNull(post.getUuid());
        assertNotNull(post.getTitle());
        assertNotNull(post.getContent());
        assertNotNull(post.getDateTime());
        assertNotNull(post.getLikesCounter());
        assertNotNull(post.getImageUrl());
    }

    @Test
    void save_shouldSavePost() throws MalformedURLException, URISyntaxException {
        PostDao post = new PostDao.Builder()
                .title("Пост4")
                .content("Контент4")
                .imageUrl(new URI("https://upload.wikimedia.org/wikipedia/commons/4/4d/Cat_November_2010-1a.jpg").toURL())
                .build();

        UUID savedPostUuid = postRepository.save(post);

        PostDao savedPost = postRepository.get(savedPostUuid);

        assertNotNull(savedPostUuid);
        assertNotNull(savedPost.getUuid());
        assertEquals("Пост4", savedPost.getTitle());
        assertEquals("Контент4", savedPost.getContent());
        assertNotNull(savedPost.getDateTime());
        assertNotNull(savedPost.getLikesCounter());
    }

    @Test
    void update_shouldUpdatePost() {
        PostDao post = new PostDao.Builder()
                .uuid(post2uuid)
                .title("Пост4")
                .content("Контент4")
                .build();

        postRepository.update(post);

        PostDao updatedPost = postRepository.get(post2uuid);

        assertNotNull(updatedPost);
        assertEquals(post2uuid, updatedPost.getUuid());
        assertEquals("Пост4", updatedPost.getTitle());
        assertEquals("Контент4", updatedPost.getContent());
    }
}
