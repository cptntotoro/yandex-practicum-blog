package ru.practicum.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.config.SecurityTestConfig;
import ru.practicum.config.TestRepositoryConfig;
import ru.practicum.service.comment.CommentService;
import ru.practicum.service.post.PostService;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes = {TestRepositoryConfig.class, SecurityTestConfig.class})
@TestPropertySource(locations="classpath:application-test.properties")
//@Import(SecurityTestConfig.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class PostRestControllerTest {
    private static final String POSTS_URL = "/posts";
    private static final String COMMENTS_URL = "/comments";
    private static final String LIKE_URL = "/like";
    private static final String INVALID_UUID = "invalid-uuid";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private CommentService commentService;

    private UUID postUuid;
    private UUID commentUuid;

    @BeforeEach
    void setUp() {
        postUuid = UUID.randomUUID();
        commentUuid = UUID.randomUUID();
    }

    @Test
    void setLike_ShouldCallServiceAndReturnAcceptedStatus() throws Exception {
        mockMvc.perform(put(POSTS_URL + "/{postUuid}" + LIKE_URL, postUuid))
                .andExpect(status().isAccepted());

        verify(postService).setLike(postUuid);
    }

    @Test
    void setLike_WithInvalidPostUuid_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put(POSTS_URL + "/{postUuid}" + LIKE_URL, INVALID_UUID))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(postService);
    }

    @Test
    void updateComment_ShouldCallServiceAndReturnOkStatus() throws Exception {
        String content = "{\"content\":\"Updated content\"}";

        mockMvc.perform(post(POSTS_URL + "/{postUuid}" + COMMENTS_URL + "/{commentUuid}",
                        postUuid, commentUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk());

        verify(commentService).update(any());
    }

    @Test
    void updateComment_WithInvalidUuids_ShouldReturnBadRequest() throws Exception {
        String content = "{\"content\":\"Updated content\"}";

        mockMvc.perform(post(POSTS_URL + "/{postUuid}" + COMMENTS_URL + "/{commentUuid}",
                        INVALID_UUID, INVALID_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(commentService);
    }

    @Test
    void deleteComment_ShouldCallServiceAndReturnNoContentStatus() throws Exception {
        mockMvc.perform(delete(POSTS_URL + "/{postUuid}" + COMMENTS_URL + "/{commentUuid}",
                        postUuid, commentUuid))
                .andExpect(status().isNoContent());

        verify(commentService).delete(commentUuid);
    }

    @Test
    void deleteComment_WithInvalidUuids_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(delete(POSTS_URL + "/{postUuid}" + COMMENTS_URL + "/{commentUuid}",
                        INVALID_UUID, INVALID_UUID))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(commentService);
    }

    @Test
    void deletePost_ShouldCallServiceAndReturnNoContentStatus() throws Exception {
        mockMvc.perform(delete(POSTS_URL + "/{postUuid}", postUuid))
                .andExpect(status().isNoContent());

        verify(postService).delete(postUuid);
    }

    @Test
    void deletePost_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(delete(POSTS_URL + "/{postUuid}", INVALID_UUID))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(postService);
    }
}