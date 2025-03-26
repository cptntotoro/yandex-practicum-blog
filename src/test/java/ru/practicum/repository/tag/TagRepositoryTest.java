package ru.practicum.repository.tag;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.practicum.config.DataSourceConfig;
import ru.practicum.config.WebConfig;
import ru.practicum.dao.tag.TagDao;
import ru.practicum.exception.tag.TagNotFoundException;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = {DataSourceConfig.class, WebConfig.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@WebAppConfiguration
class TagRepositoryTest {
    private static final String TAG_TITLE_1 = "Тег1";
    private static final String TAG_TITLE_2 = "Тег2";
    private static final String UPDATED_TAG_TITLE = "Обновленный тег";
    private static final String NEW_TAG_TITLE = "Новый тег";
    private static final String POST_TITLE_1 = "Пост1";
    private static final String POST_CONTENT_1 = "Контент поста1";
    private static final String IMAGE_URL_1 = "http://example.com/image1.jpg";
    private static final String POST_WITHOUT_TAGS_TITLE = "Пост без тегов";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TagRepository tagRepository;

    private UUID tag1Uuid;
    private UUID tag2Uuid;
    private UUID post1Uuid;
    private UUID nonExistentUuid;

    @BeforeEach
    void setUp() {
        tag1Uuid = UUID.fromString("54439ac0-a7b6-4e36-be68-3ce15cb78959");
        tag2Uuid = UUID.fromString("cd149672-29d6-4b71-a6be-fc244b8ef8b8");
        post1Uuid = UUID.fromString("c32ca081-b82a-47d8-8293-40d0af4c2632");
        nonExistentUuid = UUID.fromString("00000000-0000-0000-0000-000000000000");

        clearDatabase();
        initializeTestData();
    }

    private void clearDatabase() {
        jdbcTemplate.execute("DELETE FROM post_tags");
        jdbcTemplate.execute("DELETE FROM tags");
        jdbcTemplate.execute("DELETE FROM posts");
    }

    private void initializeTestData() {
        insertTag(tag1Uuid, TAG_TITLE_1);
        insertTag(tag2Uuid, TAG_TITLE_2);
        insertPost(post1Uuid, POST_TITLE_1, IMAGE_URL_1, POST_CONTENT_1);
        linkPostToTag(post1Uuid, tag1Uuid);
    }

    private void insertTag(UUID tagUuid, String title) {
        jdbcTemplate.update("INSERT INTO tags (tag_uuid, title) VALUES (?, ?)", tagUuid, title);
    }

    private void insertPost(UUID postUuid, String title, String imageUrl, String content) {
        jdbcTemplate.update(
                "INSERT INTO posts (post_uuid, title, image_url, text_content) VALUES (?, ?, ?, ?)",
                postUuid, title, imageUrl, content
        );
    }

    private void linkPostToTag(UUID postUuid, UUID tagUuid) {
        jdbcTemplate.update(
                "INSERT INTO post_tags (pt_uuid, post_uuid, tag_uuid) VALUES (?, ?, ?)",
                UUID.randomUUID(), postUuid, tagUuid
        );
    }

    @Test
    void get_shouldReturnTagWhenExists() {
        TagDao tag = tagRepository.get(tag1Uuid);

        assertAll(
                () -> assertNotNull(tag),
                () -> assertEquals(tag1Uuid, tag.getUuid()),
                () -> assertEquals(TAG_TITLE_1, tag.getTitle())
        );
    }

    @Test
    void get_shouldThrowExceptionWhenTagNotExists() {
        assertThrows(TagNotFoundException.class, () -> tagRepository.get(nonExistentUuid));
    }

    @Test
    void save_shouldSaveNewTagAndReturnUuid() {
        TagDao newTag = buildTag(NEW_TAG_TITLE);
        UUID savedUuid = tagRepository.save(newTag);
        TagDao savedTag = tagRepository.get(savedUuid);

        assertAll(
                () -> assertNotNull(savedUuid),
                () -> assertEquals(NEW_TAG_TITLE, savedTag.getTitle())
        );
    }

    @Test
    void update_shouldUpdateExistingTag() {
        TagDao updatedTag = buildTag(tag2Uuid, UPDATED_TAG_TITLE);
        tagRepository.update(updatedTag);
        TagDao result = tagRepository.get(tag2Uuid);

        assertEquals(UPDATED_TAG_TITLE, result.getTitle());
    }

    @Test
    void update_shouldThrowExceptionWhenTagNotExists() {
        TagDao nonExistentTag = buildTag(nonExistentUuid, "Несуществующий тег");
        assertThrows(TagNotFoundException.class, () -> tagRepository.update(nonExistentTag));
    }

    @Test
    void deleteBy_shouldDeleteExistingTag() {
        tagRepository.deleteBy(tag1Uuid);
        assertThrows(TagNotFoundException.class, () -> tagRepository.get(tag1Uuid));
    }

    @Test
    void deleteBy_shouldThrowExceptionWhenTagNotExists() {
        assertThrows(TagNotFoundException.class, () -> tagRepository.deleteBy(nonExistentUuid));
    }

    @Test
    void getAll_shouldReturnAllTags() {
        List<TagDao> tags = tagRepository.getAll();

        assertAll(
                () -> assertEquals(2, tags.size()),
                () -> assertTrue(tags.stream().anyMatch(t -> t.getUuid().equals(tag1Uuid))),
                () -> assertTrue(tags.stream().anyMatch(t -> t.getUuid().equals(tag2Uuid)))
        );
    }

    @Test
    void getAllByPostUuid_shouldReturnTagsForPost() {
        List<TagDao> tags = tagRepository.getAllBy(post1Uuid);

        assertAll(
                () -> assertEquals(1, tags.size()),
                () -> assertEquals(tag1Uuid, tags.getFirst().getUuid())
        );
    }

    @Test
    void getAllByPostUuid_shouldReturnEmptyListForPostWithoutTags() {
        UUID postWithoutTags = UUID.randomUUID();
        insertPost(postWithoutTags, POST_WITHOUT_TAGS_TITLE, null, null);

        List<TagDao> tags = tagRepository.getAllBy(postWithoutTags);
        assertTrue(tags.isEmpty());
    }

    @Test
    void deleteAllByPostUuid_shouldRemoveAllPostTags() {
        tagRepository.deleteAllBy(post1Uuid);
        assertEquals(0, tagRepository.getAllBy(post1Uuid).size());
    }

    @Test
    void deleteAllByPostUuid_shouldThrowExceptionWhenPostNotExists() {
        assertThrows(TagNotFoundException.class, () -> tagRepository.deleteAllBy(nonExistentUuid));
    }

    @Test
    void batchUpdatePostTags_shouldSaveMultipleTagsForPost() {
        UUID newPostUuid = UUID.randomUUID();
        insertPost(newPostUuid, "Новый пост", null, null);

        tagRepository.batchUpdatePostTags(newPostUuid, List.of(tag1Uuid, tag2Uuid));
        assertEquals(2, tagRepository.getAllBy(newPostUuid).size());
    }

    private TagDao buildTag(String title) {
        return new TagDao.Builder().title(title).build();
    }

    private TagDao buildTag(UUID uuid, String title) {
        return new TagDao.Builder().uuid(uuid).title(title).build();
    }
}