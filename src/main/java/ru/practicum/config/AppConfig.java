package ru.practicum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import ru.practicum.comment.service.CommentService;

@Configuration
@Profile("prod")
@Import({WebConfig.class, DefaultRepositoryConfig.class})
public class AppConfig {
    @Bean
    public CommentService commentService() {
        return new CommentService();
    }
}
