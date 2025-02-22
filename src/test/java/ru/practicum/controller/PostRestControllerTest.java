package ru.practicum.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.controller.PostRestController;
import ru.practicum.service.post.PostService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PostRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PostService postService;

    @InjectMocks
    private PostRestController postRestController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(postRestController).build();
    }

    @Test
    void setLike_ShouldCallServiceAndReturnAcceptedStatus() throws Exception {
        UUID postUuid = UUID.randomUUID();
        mockMvc.perform(put("/posts/{postUuid}/like", postUuid))
                .andExpect(status().isAccepted());
        verify(postService, times(1)).setLike(postUuid);
    }

    @Test
    void setLike_WithInvalidPostUuid_ShouldReturnBadRequest() throws Exception {
        String invalidPostUuid = "invalid-uuid";

        mockMvc.perform(put("/posts/{postUuid}/like", invalidPostUuid))
                .andExpect(status().isBadRequest());
        verify(postService, times(0)).setLike(any());
    }
}