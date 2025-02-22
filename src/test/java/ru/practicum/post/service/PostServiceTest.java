package ru.practicum.post.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.config.AppConfig;
import ru.practicum.post.Post;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
public class PostServiceTest {
    @Autowired
    private PostService postService;

    @Test
    public void testAddPost() {
        Post post = new Post();
        post.setTitle("Test Post");
        postService.add(post);

        Post foundPost = postService.getById(post.getId());
        assertNotNull(foundPost);
        assertEquals("Test Post", foundPost.getTitle());
    }
}
