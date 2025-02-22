package ru.practicum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.practicum.comment.service.CommentService;

@Configuration
@Profile("test")
public class AppTestConfig {
    @Bean
    public CommentService commentService() {
        return new CommentService();
    }
}
