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
import ru.practicum.exception.post.PostNotFoundException;

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
class PostRepositoryTest {
    private static final String POST_TITLE_1 = "Название поста1";
    private static final String POST_TITLE_2 = "Название поста2";
    private static final String POST_TITLE_3 = "Название поста3";
    private static final String POST_CONTENT_1 = "Контент поста1";
    private static final String POST_CONTENT_2 = "Контент поста2";
    private static final String POST_CONTENT_3 = "Контент поста3";
    private static final String IMAGE_URL_1 = "https://example.com/image1.jpg";
    private static final String IMAGE_URL_3 = "https://example.com/image3.jpg";
    private static final String NEW_POST_TITLE = "Новый пост";
    private static final String NEW_POST_CONTENT = "Контент нового поста";
    private static final String NEW_IMAGE_URL = "https://example.com/new.jpg";
    private static final String UPDATED_POST_TITLE = "Обновленный пост";
    private static final String UPDATED_POST_CONTENT = "Обновленный контент";
    private static final String UPDATED_IMAGE_URL = "https://example.com/updated.jpg";
    private static final String TAG_TITLE_1 = "Тег1";
    private static final String TAG_TITLE_2 = "Тег2";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PostRepository postRepository;

    private UUID post1uuid;
    private UUID post2uuid;
    private UUID post3uuid;
    private UUID tag1uuid;
    private UUID tag2uuid;
    private UUID nonExistentUuid;

    @BeforeEach
    void setUp() {
        post1uuid = UUID.randomUUID();
        post2uuid = UUID.randomUUID();
        post3uuid = UUID.randomUUID();
        tag1uuid = UUID.randomUUID();
        tag2uuid = UUID.randomUUID();
        nonExistentUuid = UUID.randomUUID();

        clearDatabase();
        initializeTestData();
    }

    private void clearDatabase() {
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM post_tags");
        jdbcTemplate.execute("DELETE FROM tags");
        jdbcTemplate.execute("DELETE FROM posts");
    }

    private void initializeTestData() {
        insertPost(post1uuid, POST_TITLE_1, IMAGE_URL_1, POST_CONTENT_1, 10, LocalDateTime.now());
        insertPost(post2uuid, POST_TITLE_2, null, POST_CONTENT_2, 5, LocalDateTime.now().minusDays(1));
        insertPost(post3uuid, POST_TITLE_3, IMAGE_URL_3, POST_CONTENT_3, 15, LocalDateTime.now().minusDays(2));

        insertTag(tag1uuid, TAG_TITLE_1);
        insertTag(tag2uuid, TAG_TITLE_2);

        linkPostToTag(post1uuid, tag1uuid);
        linkPostToTag(post2uuid, tag2uuid);
        linkPostToTag(post3uuid, tag1uuid);
    }

    private void insertPost(UUID postUuid, String title, String imageUrl, String content, int likes, LocalDateTime dateTime) {
        jdbcTemplate.update(
                "INSERT INTO posts (post_uuid, title, image_url, text_content, date_time, likes) VALUES (?, ?, ?, ?, ?, ?)",
                postUuid, title, imageUrl, content, dateTime, likes
        );
    }

    private void insertTag(UUID tagUuid, String title) {
        jdbcTemplate.update(
                "INSERT INTO tags (tag_uuid, title) VALUES (?, ?)",
                tagUuid, title
        );
    }

    private void linkPostToTag(UUID postUuid, UUID tagUuid) {
        jdbcTemplate.update(
                "INSERT INTO post_tags (pt_uuid, post_uuid, tag_uuid) VALUES (?, ?, ?)",
                UUID.randomUUID(), postUuid, tagUuid
        );
    }

    @Test
    void get_shouldReturnPostWhenExists() {
        PostDao post = postRepository.get(post1uuid);

        assertAll(
                () -> assertNotNull(post),
                () -> assertEquals(post1uuid, post.getUuid()),
                () -> assertEquals(POST_TITLE_1, post.getTitle()),
                () -> assertEquals(POST_CONTENT_1, post.getContent()),
                () -> assertEquals(10, post.getLikesCounter())
        );
    }

    @Test
    void get_shouldThrowExceptionWhenPostNotExists() {
        assertThrows(PostNotFoundException.class, () -> postRepository.get(nonExistentUuid));
    }

    @Test
    void save_shouldSaveNewPostAndReturnUuid() throws MalformedURLException, URISyntaxException {
        PostDao newPost = buildPost(NEW_POST_TITLE, NEW_POST_CONTENT, NEW_IMAGE_URL);

        UUID savedUuid = postRepository.save(newPost);
        PostDao savedPost = postRepository.get(savedUuid);

        assertAll(
                () -> assertNotNull(savedUuid),
                () -> assertEquals(NEW_POST_TITLE, savedPost.getTitle()),
                () -> assertEquals(NEW_POST_CONTENT, savedPost.getContent())
        );
    }

    @Test
    void update_shouldUpdateExistingPost() throws MalformedURLException, URISyntaxException {
        PostDao updatedPost = buildPost(post1uuid, UPDATED_POST_TITLE, UPDATED_POST_CONTENT, UPDATED_IMAGE_URL);

        postRepository.update(updatedPost);
        PostDao result = postRepository.get(post1uuid);

        assertAll(
                () -> assertEquals(UPDATED_POST_TITLE, result.getTitle()),
                () -> assertEquals(UPDATED_POST_CONTENT, result.getContent()),
                () -> assertEquals(UPDATED_IMAGE_URL, result.getImageUrl().toString())
        );
    }

    @Test
    void update_shouldThrowExceptionWhenPostNotExists() throws MalformedURLException, URISyntaxException {
        PostDao nonExistentPost = buildPost(nonExistentUuid, "Несуществующий пост", "Контент", null);

        assertThrows(PostNotFoundException.class, () -> postRepository.update(nonExistentPost));
    }

    @Test
    void deleteBy_shouldDeleteExistingPost() {
        postRepository.deleteBy(post1uuid);

        assertThrows(PostNotFoundException.class, () -> postRepository.get(post1uuid));
    }

    @Test
    void deleteBy_shouldThrowExceptionWhenPostNotExists() {
        assertThrows(PostNotFoundException.class, () -> postRepository.deleteBy(nonExistentUuid));
    }

    @Test
    void getAllByTag_shouldReturnPostsWithTag() {
        List<PostDao> posts = postRepository.getAllBy(tag1uuid);

        assertAll(
                () -> assertEquals(2, posts.size()),
                () -> assertTrue(posts.stream().anyMatch(p -> p.getUuid().equals(post1uuid))),
                () -> assertTrue(posts.stream().anyMatch(p -> p.getUuid().equals(post3uuid)))
        );
    }

    @Test
    void setLike_shouldIncrementLikesCounter() {
        int initialLikes = postRepository.get(post1uuid).getLikesCounter();

        postRepository.setLike(post1uuid);
        int updatedLikes = postRepository.get(post1uuid).getLikesCounter();

        assertEquals(initialLikes + 1, updatedLikes);
    }

    @Test
    void setLike_shouldThrowExceptionWhenPostNotExists() {
        assertThrows(PostNotFoundException.class, () -> postRepository.setLike(nonExistentUuid));
    }

    @Test
    void getPage_shouldReturnPostsWithPagination() {
        List<PostDao> posts = postRepository.getPage(1, 2);

        assertAll(
                () -> assertEquals(2, posts.size()),
                () -> assertTrue(posts.getFirst().getDateTime().isAfter(posts.get(1).getDateTime()))
        );
    }

    @Test
    void getPage_shouldReturnEmptyListForInvalidPage() {
        List<PostDao> posts = postRepository.getPage(10, 2);

        assertTrue(posts.isEmpty());
    }

    @Test
    void getTotal_shouldReturnTotalPostsCount() {
        assertEquals(3, postRepository.getTotal());
    }

    @Test
    void getTotal_shouldReturnZeroWhenNoPosts() {
        clearDatabase();
        assertEquals(0, postRepository.getTotal());
    }

    @Test
    void isExist_shouldReturnTrueForExistingPost() {
        assertTrue(postRepository.isExist(post1uuid));
    }

    @Test
    void isExist_shouldReturnFalseForNonExistingPost() {
        assertFalse(postRepository.isExist(nonExistentUuid));
    }

    private PostDao buildPost(String title, String content, String imageUrl) throws MalformedURLException, URISyntaxException {
        return new PostDao.Builder()
                .title(title)
                .content(content)
                .imageUrl(imageUrl != null ? new URI(imageUrl).toURL() : null)
                .build();
    }

    private PostDao buildPost(UUID uuid, String title, String content, String imageUrl) throws MalformedURLException, URISyntaxException {
        return new PostDao.Builder()
                .uuid(uuid)
                .title(title)
                .content(content)
                .imageUrl(imageUrl != null ? new URI(imageUrl).toURL() : null)
                .build();
    }
}