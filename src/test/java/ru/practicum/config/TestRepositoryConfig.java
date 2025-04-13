package ru.practicum.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.practicum.repository.comment.CommentRepository;
import ru.practicum.repository.comment.CommentRepositoryJdbc;
import ru.practicum.repository.post.PostRepository;
import ru.practicum.repository.post.PostRepositoryJdbc;
import ru.practicum.repository.tag.TagRepository;
import ru.practicum.repository.tag.TagRepositoryJdbc;

@TestConfiguration
@Profile("test")
public class TestRepositoryConfig {

    @Bean
    public CommentRepository commentRepository(JdbcTemplate jdbcTemplate) {
        return new CommentRepositoryJdbc(jdbcTemplate);
    }

    @Bean
    public PostRepository postRepository(JdbcTemplate jdbcTemplate) {
        return new PostRepositoryJdbc(jdbcTemplate);
    }

    @Bean
    public TagRepository tagRepository(JdbcTemplate jdbcTemplate) {
        return new TagRepositoryJdbc(jdbcTemplate);
    }
}
