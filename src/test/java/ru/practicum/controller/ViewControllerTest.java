package ru.practicum.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.model.post.Post;
import ru.practicum.model.tag.Tag;
import ru.practicum.service.post.PostService;
import ru.practicum.service.tag.TagService;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

//import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ViewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PostService postService;

    @Mock
    private TagService tagService;

    @InjectMocks
    private ViewController viewController;

    private final UUID postUuid = UUID.randomUUID();

    private final Post post = new Post.Builder()
            .title("Test Title")
            .content("Test Content")
            .imageUrl(URI.create("http://example.com/image.jpg").toURL())
            .tags(List.of(new Tag.Builder().uuid(postUuid).build()))
            .build();

    ViewControllerTest() throws MalformedURLException {}

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(viewController).build();
    }

    @Test
    void getAllByPage_ShouldReturnNewsfeedView() throws Exception {
        when(postService.getPage(anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());

        when(tagService.getAll())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/posts")
                        .param("from", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attributeExists("tags"))
                .andExpect(view().name("newsfeed"));

        verify(postService, times(1)).getPage(1, 10);
    }

    @Test
    void getByUuid_ShouldReturnPostView() throws Exception {
        when(postService.get(any()))
                .thenReturn(post);

        mockMvc.perform(get("/posts/" + postUuid))
                .andExpect(status().isOk())
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"));

        verify(postService, times(1)).get(any());
    }

    @Test
    void add_ShouldCreatePostAndRedirect() throws Exception {
        when(postService.save(any()))
                .thenReturn(post);

        mockMvc.perform(post("/posts")
                        .param("title", "Test Title")
                        .param("content", "Test Content")
                        .param("imageUrl", "http://example.com/image.jpg")
                        .param("tags", postUuid.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("post"));

        verify(postService, times(1)).save(any());
    }

    @Test
    void update_ShouldUpdatePostAndRedirect() throws Exception {
        when(postService.update(any()))
                .thenReturn(post);

        mockMvc.perform(post("/posts/" + postUuid)
                        .param("title", "Test Title")
                        .param("content", "Test Content")
                        .param("imageUrl", "http://example.com/image.jpg")
                        .param("tags", postUuid.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("post"));

        verify(postService, times(1)).update(any());
    }

    @Test
    void getAllByTag_ShouldReturnNewsfeedView() throws Exception {
        UUID tagUuid = UUID.randomUUID();

        when(postService.getAllByTag(tagUuid))
                .thenReturn(Collections.emptyList());

        when(tagService.getAll())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/posts/tag/{tagUuid}", tagUuid))
                .andExpect(status().isOk())
                .andExpect(view().name("newsfeed"));

        verify(postService, times(1)).getAllByTag(tagUuid);
    }
}