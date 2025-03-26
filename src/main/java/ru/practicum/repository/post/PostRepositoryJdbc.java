package ru.practicum.repository.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dao.post.PostDao;
import ru.practicum.exception.post.PostNotFoundException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Репозиторий постов
 */
@Repository
public class PostRepositoryJdbc implements PostRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String sqlGetPostByUuid = """
        SELECT *
        FROM posts
        WHERE post_uuid = ?
    """;

    private static final String sqlGetAllPostsPage = """
        SELECT rs.*
        FROM (select p.*, row_number() over (ORDER BY date_time DESC) as row_num
            FROM posts p) rs
        WHERE rs.row_num >= ? and rs.row_num  < ?
        ORDER BY rs.row_num
    """;

    private static final String sqlGetAllPostsByTag = """
        SELECT *
        FROM posts p
        JOIN post_tags pt ON p.post_uuid = pt.post_uuid
        WHERE pt.tag_uuid = ?
        ORDER BY date_time
    """;

    private static final String sqlSavePost = """
        INSERT INTO posts (title, image_url, text_content)
        VALUES (?, ?, ?)
    """;

    private static final String sqlDeletePostByUuid = """
        DELETE FROM posts
        WHERE post_uuid = ?
    """;

    private static final String sqlUpdatePostByUuid = """
        UPDATE posts
        SET title = ?, image_url = ?, text_content = ?
        WHERE post_uuid = ?
    """;

    private static final String sqlSetLikeByPostUuid = """
        UPDATE posts
        SET likes = likes + 1
        WHERE post_uuid = ?
    """;

    private static final String sqlGetTotal = """
        SELECT COUNT(*) FROM posts
    """;

    private static final String sqlCountPostByUuid = """
        SELECT COUNT(*) FROM posts WHERE post_uuid = ?
    """;

    @Override
    public PostDao get(UUID uuid) {
        try {
            return jdbcTemplate.queryForObject(sqlGetPostByUuid, POST_ROW_MAPPER, uuid);
        } catch (EmptyResultDataAccessException ex) {
            throw new PostNotFoundException("Пост с uuid=" + uuid + " не найден");
        }
    }

    @Override
    public UUID save(PostDao post) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlSavePost, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getImageUrl().toString());
            ps.setString(3, post.getContent());
            return ps;
        }, keyHolder);

        return (UUID) Objects.requireNonNull(keyHolder.getKeys()).get("post_uuid");
    }

    @Override
    public void update(PostDao post) {
        URL imageUrl = post.getImageUrl();

        int rowsUpdated = jdbcTemplate.update(sqlUpdatePostByUuid,
                post.getTitle(),
                imageUrl == null ? null : imageUrl.toString(),
                post.getContent(),
                post.getUuid()
        );

        if (rowsUpdated == 0) {
            throw new PostNotFoundException("Пост с uuid=" + post.getUuid() + " не был обновлен. Пост с таким uuid не найден");
        }
    }

    @Override
    public void deleteBy(UUID uuid) {
        int rowsUpdated = jdbcTemplate.update(sqlDeletePostByUuid, uuid);

        if (rowsUpdated == 0) {
            throw new PostNotFoundException("Пост с id=" + uuid + " не был удален. Пост с таким uuid не найден");
        }
    }

    @Override
    @Transactional
    public List<PostDao> getAllBy(UUID tagUuid) {
        return jdbcTemplate.query(sqlGetAllPostsByTag, POST_ROW_MAPPER, tagUuid);
    }

    @Override
    public void setLike(UUID postUUID) {
        int rowsUpdated = jdbcTemplate.update(sqlSetLikeByPostUuid, postUUID);

        if (rowsUpdated == 0) {
            throw new PostNotFoundException("Число лайков у поста с guid=" + postUUID + " не был обновлен. Пост с таким uuid не найден");
        }
    }

    @Override
    public List<PostDao> getPage(int page, int size) {
        int start = (page - 1) * size + 1; // Начальный индекс
        int end = start + size; // Конечный индекс

        return jdbcTemplate.query(
                sqlGetAllPostsPage,
                POST_ROW_MAPPER,
                start,
                end
        );
    }

    @Override
    public long getTotal() {
        Long count = jdbcTemplate.queryForObject(sqlGetTotal, Long.class);
        return count != null ? count : 0;
    }

    @Override
    public boolean isExist(UUID postUuid) {
        Integer postCount = jdbcTemplate.queryForObject(
                sqlCountPostByUuid,
                Integer.class,
                postUuid
        );

        return postCount != null && postCount > 0;
    }

    private static final RowMapper<PostDao> POST_ROW_MAPPER = (rs, rowNum) -> {
        String imageUrl = rs.getString("image_url");

        try {
            return new PostDao.Builder()
                    .uuid(UUID.fromString(rs.getString("post_uuid")))
                    .title(rs.getString("title"))
                    .content(rs.getString("text_content"))
                    .preview(rs.getString("text_preview"))
                    .imageUrl(imageUrl == null ? null : new URI(imageUrl).toURL())
                    .likesCounter(rs.getInt("likes"))
                    .dateTime(rs.getObject("date_time", LocalDateTime.class))
                    .build();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    };
}
