package ru.practicum.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.dto.comment.CommentAddDto;
import ru.practicum.model.post.Post;
import ru.practicum.model.tag.Tag;
import ru.practicum.service.comment.CommentService;
import ru.practicum.service.post.PostService;
import ru.practicum.service.tag.TagService;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ViewControllerTest {
    private static final String POSTS_URL = "/posts";
    private static final String COMMENTS_URL = "/comments";
    private static final String TAG_URL = "/tag";
    private static final String NEWSFEED_VIEW = "newsfeed";
    private static final String POST_VIEW = "post";
    private static final String TEST_TITLE = "Test Title";
    private static final String TEST_CONTENT = "Test Content";
    private static final String IMAGE_URL = "http://example.com/image.jpg";
    private static final String TEST_COMMENT = "Test comment";

    private MockMvc mockMvc;
    private UUID postUuid;
    private Post post;

    @Mock
    private PostService postService;
    @Mock
    private CommentService commentService;
    @Mock
    private TagService tagService;
    @InjectMocks
    private ViewController viewController;

    @BeforeEach
    void setUp() throws MalformedURLException {
        mockMvc = MockMvcBuilders.standaloneSetup(viewController).build();
        postUuid = UUID.randomUUID();
        post = new Post.Builder()
                .title(TEST_TITLE)
                .content(TEST_CONTENT)
                .imageUrl(URI.create(IMAGE_URL).toURL())
                .tags(List.of(new Tag.Builder().uuid(postUuid).build()))
                .build();
    }

    @Test
    void getAllByPage_ShouldReturnNewsfeedView() throws Exception {
        when(postService.getPage(anyInt(), anyInt())).thenReturn(Collections.emptyList());
        when(tagService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get(POSTS_URL)
                        .param("from", "1")
                        .param("size", "10"))
                .andExpectAll(
                        status().isOk(),
                        model().attributeExists("posts", "tags"),
                        view().name(NEWSFEED_VIEW));

        verify(postService).getPage(1, 10);
    }

    @Test
    void getByUuid_ShouldReturnPostView() throws Exception {
        when(postService.get(postUuid)).thenReturn(post);

        mockMvc.perform(get(POSTS_URL + "/{postUuid}", postUuid))
                .andExpectAll(
                        status().isOk(),
                        view().name(POST_VIEW),
                        model().attributeExists("post"));

        verify(postService).get(postUuid);
    }

    @Test
    void add_ShouldCreatePostAndRedirect() throws Exception {
        when(postService.save(any(Post.class))).thenReturn(post);

        mockMvc.perform(post(POSTS_URL)
                        .param("title", TEST_TITLE)
                        .param("content", TEST_CONTENT)
                        .param("imageUrl", IMAGE_URL)
                        .param("tags", postUuid.toString()))
                .andExpectAll(
                        status().is3xxRedirection(),
                        flash().attributeExists("post"));

        verify(postService).save(any(Post.class));
    }

    @Test
    void addComment_ShouldSaveCommentAndRedirect() throws Exception {
        when(postService.get(postUuid)).thenReturn(post);

        mockMvc.perform(post(POSTS_URL + "/{postUuid}" + COMMENTS_URL, postUuid)
                        .flashAttr("newComment", new CommentAddDto(TEST_COMMENT)))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl(POSTS_URL + "/" + postUuid),
                        flash().attributeExists("post"));

        verify(commentService).save(any());
        verify(postService).get(postUuid);
    }

    @Test
    void update_ShouldUpdatePostAndRedirect() throws Exception {
        when(postService.update(any(Post.class))).thenReturn(post);

        mockMvc.perform(post(POSTS_URL + "/{postUuid}", postUuid)
                        .param("title", TEST_TITLE)
                        .param("content", TEST_CONTENT)
                        .param("imageUrl", IMAGE_URL)
                        .param("tags", postUuid.toString()))
                .andExpectAll(
                        status().is3xxRedirection(),
                        flash().attributeExists("post"));

        verify(postService).update(any(Post.class));
    }

    @Test
    void getAllByTag_ShouldReturnNewsfeedView() throws Exception {
        UUID tagUuid = UUID.randomUUID();

        when(postService.getAllByTag(tagUuid)).thenReturn(Collections.emptyList());
        when(tagService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get(POSTS_URL + TAG_URL + "/{tagUuid}", tagUuid))
                .andExpectAll(
                        status().isOk(),
                        view().name(NEWSFEED_VIEW));

        verify(postService).getAllByTag(tagUuid);
    }
}