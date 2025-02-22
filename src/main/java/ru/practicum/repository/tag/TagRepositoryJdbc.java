package ru.practicum.repository.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.practicum.dao.tag.TagDao;
import ru.practicum.exception.tag.TagNotFoundException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Репозиторий тегов
 */
@Repository
public class TagRepositoryJdbc implements TagRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String sqlGetTagByUuid = """
        SELECT *
        FROM tags
        WHERE tag_uuid = ?
    """;

    private static final String sqlSaveTag = """
        INSERT INTO tags (title)
        VALUES (?)
    """;

    private static final String sqlUpdateTagByUuid = """
        UPDATE tags
        SET title = ?
        WHERE tag_uuid = ?
    """;

    private static final String sqlDeleteTagByUuid = """
        DELETE FROM tags
        WHERE tag_uuid = ?
    """;

    private static final String sqlGetAllTags = """
        SELECT *
        FROM tags
    """;

    private static final String sqlGetAllByPostUuid = """
        SELECT t.*
        FROM post_tags pt
        JOIN tags t ON pt.tag_uuid = t.tag_uuid
        WHERE pt.post_uuid = ?
    """;

    private static final String sqlDeleteAllByPostUuid = """
        DELETE FROM post_tags
        WHERE post_uuid = ?
    """;

    private static final String sqlSavePostTags = """
        INSERT INTO post_tags (pt_uuid, post_uuid, tag_uuid)
        VALUES (?, ?, ?)
    """;

    @Override
    public TagDao get(UUID uuid) {
        try {
            return jdbcTemplate.queryForObject(sqlGetTagByUuid, TAG_ROW_MAPPER, uuid);
        } catch (EmptyResultDataAccessException ex) {
            throw new TagNotFoundException("Тег с uuid=" + uuid + " не найден");
        }
    }

    @Override
    public UUID save(TagDao tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlSaveTag, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tag.getTitle());
            return ps;
        }, keyHolder);

        return (UUID) Objects.requireNonNull(keyHolder.getKeys()).get("tag_uuid");
    }

    @Override
    public void update(TagDao tag) {
        UUID tagUuid = tag.getUuid();
        int rowsUpdated = jdbcTemplate.update(sqlUpdateTagByUuid, tag.getTitle(), tag.getUuid());

        if (rowsUpdated == 0) {
            throw new TagNotFoundException("Тег с guid=" + tagUuid + " не был обновлен. Тег с таким uuid не найден");
        }
    }

    @Override
    public void deleteBy(UUID uuid) {
        int rowsUpdated = jdbcTemplate.update(sqlDeleteTagByUuid, uuid);

        if (rowsUpdated == 0) {
            throw new TagNotFoundException("Тег с uuid=" + uuid + " не был удален. Тег с таким uuid не найден");
        }
    }

    @Override
    public void deleteAllBy(UUID postUuid) {
        int rowsUpdated = jdbcTemplate.update(sqlDeleteAllByPostUuid, postUuid);

        if (rowsUpdated == 0) {
            throw new TagNotFoundException("Теги поста с uuid=" + postUuid + " не удалены. Пост с таким uuid не найден");
        }
    }

    @Override
    public List<TagDao> getAll() {
        return jdbcTemplate.query(sqlGetAllTags, TAG_ROW_MAPPER);
    }

    @Override
    public void batchUpdatePostTags(UUID postUuid, List<UUID> postTagUuids) {
        jdbcTemplate.batchUpdate(sqlSavePostTags, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setObject(1, UUID.randomUUID());
                ps.setObject(2, postUuid);
                ps.setObject(3, postTagUuids.get(i));
            }

            @Override
            public int getBatchSize() {
                return postTagUuids.size();
            }
        });
    }

    @Override
    public List<TagDao> getAllBy(UUID uuid) {
        return jdbcTemplate.query(sqlGetAllByPostUuid, TAG_ROW_MAPPER, uuid);
    }

    private static final RowMapper<TagDao> TAG_ROW_MAPPER = (rs, rowNum) -> new TagDao.Builder()
            .uuid(UUID.fromString(rs.getString("tag_uuid")))
            .title(rs.getString("title"))
            .build();
}
