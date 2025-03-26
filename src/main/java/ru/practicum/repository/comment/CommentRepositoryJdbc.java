package ru.practicum.repository.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.practicum.dao.comment.CommentDao;
import ru.practicum.exception.comment.CommentNotFoundException;
import ru.practicum.exception.post.PostNotFoundException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Repository
public class CommentRepositoryJdbc implements CommentRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String sqlAddCommentToPostByUuid = """
        WITH post_check AS (
            SELECT 1 FROM posts WHERE post_uuid = ? LIMIT 1
        )
        INSERT INTO comments (post_uuid, text_content)
        SELECT ?, ?
        FROM post_check
        WHERE EXISTS (SELECT 1 FROM post_check)
    """;

    private static final String sqlUpdateCommentByUuid = """
        UPDATE comments c
        SET text_content = ?
        FROM posts p
        WHERE c.comment_uuid = ?
        AND c.post_uuid = p.post_uuid
    """;

    private static final String sqlGetCommentByUuid = """
        SELECT *
        FROM comments
        WHERE comment_uuid = ?
    """;

    private static final String sqlGetAllCommentsByPostUuid = """
        SELECT *
        FROM comments
        WHERE post_uuid = ?
        ORDER BY date_time DESC
    """;

    private static final String sqlDeleteCommentByUuid = """
        DELETE FROM comments
        WHERE comment_uuid = ?
    """;

    @Override
    public UUID save(CommentDao comment) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsUpdated = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sqlAddCommentToPostByUuid,
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setObject(1, comment.getPostUuid());
            ps.setObject(2, comment.getPostUuid());
            ps.setString(3, comment.getContent());
            return ps;
        }, keyHolder);

        if (rowsUpdated == 0) {
            throw new PostNotFoundException("Пост с uuid=" + comment.getPostUuid() + " не найден");
        }

        return (UUID) Objects.requireNonNull(keyHolder.getKeys()).get("comment_uuid");
    }

    @Override
    public void update(CommentDao comment) {
        int rowsUpdated = jdbcTemplate.update(
                sqlUpdateCommentByUuid,
                comment.getContent(),
                comment.getUuid()
        );

        if (rowsUpdated == 0) {
            throw new CommentNotFoundException(
                    "Комментарий с uuid=" + comment.getUuid() + " не обновлен. " +
                            "Либо комментарий не существует, либо пост был удален"
            );
        }
    }

    @Override
    public CommentDao get(UUID uuid) {
        try {
            return jdbcTemplate.queryForObject(sqlGetCommentByUuid, COMMENT_ROW_MAPPER, uuid);
        } catch (EmptyResultDataAccessException ex) {
            throw new CommentNotFoundException("Комментарий с uuid=" + uuid + " не найден");
        }
    }

    @Override
    public void deleteBy(UUID uuid) {
        int rowsUpdated = jdbcTemplate.update(sqlDeleteCommentByUuid, uuid);

        if (rowsUpdated == 0) {
            throw new CommentNotFoundException("Комментарий с id=" + uuid + " не был удален. Комментарий с таким uuid не найден");
        }
    }

    @Override
    public List<CommentDao> getAllBy(UUID postUuid) {
        return jdbcTemplate.query(sqlGetAllCommentsByPostUuid, COMMENT_ROW_MAPPER, postUuid);
    }

    private static final RowMapper<CommentDao> COMMENT_ROW_MAPPER = (rs, rowNum) -> new CommentDao.Builder()
            .uuid(UUID.fromString(rs.getString("comment_uuid")))
            .postUuid(UUID.fromString(rs.getString("post_uuid")))
            .content((rs.getString("text_content")))
            .dateTime((rs.getObject("date_time", LocalDateTime.class)))
            .build();
}
