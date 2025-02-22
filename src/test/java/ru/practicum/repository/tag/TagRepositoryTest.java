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

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitConfig(classes = {DataSourceConfig.class, WebConfig.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@WebAppConfiguration
public class TagRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TagRepository tagRepository;

    private static final UUID tag1uuid = UUID.fromString("54439ac0-a7b6-4e36-be68-3ce15cb78959");
    private static final UUID tag2uuid = UUID.fromString("cd149672-29d6-4b71-a6be-fc244b8ef8b8");
    private static final UUID post1uuid = UUID.fromString("c32ca081-b82a-47d8-8293-40d0af4c2632");

    @BeforeEach
    public void setUp() {
        jdbcTemplate.execute("DELETE FROM tags");
        jdbcTemplate.execute("DELETE FROM posts");

        String sqlAddTag = "INSERT INTO tags (tag_uuid, title) VALUES (?, ?)";

        jdbcTemplate.update(sqlAddTag, tag1uuid, "Тег1");
        jdbcTemplate.update(sqlAddTag, tag2uuid, "Тег2");

        String sqlAddPost = "INSERT INTO posts (post_uuid, title, image_url, text_content) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sqlAddPost, post1uuid, "Описание поста1", "https://upload.wikimedia.org/wikipedia/commons/4/4d/Cat_November_2010-1a.jpg", "Контент поста1");

        String sqlAddPostTag = "INSERT INTO post_tags (pt_uuid, post_uuid, tag_uuid) VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlAddPostTag, UUID.randomUUID(), post1uuid, tag1uuid);
    }

    @Test
    void getAll_shouldReturnTags() {
        List<TagDao> tags = tagRepository.getAll().stream().toList();

        assertNotNull(tags);
        assertEquals(2, tags.size());
    }

    @Test
    void get_shouldReturnTag() {
        TagDao tag = tagRepository.get(tag1uuid);
        assertNotNull(tag);
    }

    @Test
    void save_shouldSaveTag() {
        TagDao tag = new TagDao.Builder()
                .title("Тег4")
                .build();

        UUID savedTagUuid = tagRepository.save(tag);

        TagDao savedTag = tagRepository.get(savedTagUuid);

        assertNotNull(savedTagUuid);
        assertNotNull(savedTag);
        assertNotNull(savedTag.getUuid());
        assertEquals("Тег4", savedTag.getTitle());
    }

    @Test
    void update_shouldUpdateTag() {
        TagDao tag = new TagDao.Builder()
                .uuid(tag2uuid)
                .title("ТегНовый")
                .build();

        tagRepository.update(tag);

        TagDao updatedTag = tagRepository.get(tag2uuid);

        assertNotNull(updatedTag);
        assertEquals(tag2uuid, updatedTag.getUuid());
        assertEquals("ТегНовый", updatedTag.getTitle());
    }

    @Test
    void getAllByPostUuid_shouldReturnTags() {
        List<TagDao> tags = tagRepository.getAllBy(post1uuid).stream().toList();

        assertNotNull(tags);
        assertEquals(1, tags.size());
        assertEquals(tag1uuid, tags.getFirst().getUuid());
    }
}
