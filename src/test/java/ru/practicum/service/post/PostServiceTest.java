package ru.practicum.service.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.mapper.post.PostDaoMapper;
import ru.practicum.model.post.Post;
import ru.practicum.model.post.PostPreview;
import ru.practicum.model.tag.Tag;
import ru.practicum.repository.post.PostRepository;
import ru.practicum.service.comment.CommentService;
import ru.practicum.service.tag.TagService;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class PostServiceTest {
    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private TagService tagService;

    @Mock
    private CommentService commentService;

    private Post post;
    private Post savedPost;
    private final UUID postUuid = UUID.randomUUID();
    private final URL imageUrl;

    public PostServiceTest() throws URISyntaxException, MalformedURLException {
        this.imageUrl = new URI("https://upload.wikimedia.org/wikipedia/commons/4/4d/Cat_November_2010-1a.jpg").toURL();
    }

    @BeforeEach
    void setup() {
        post = new Post.Builder()
                .title("Заголовок")
                .content("Контент")
                .imageUrl(imageUrl)
                .tags(List.of(new Tag.Builder().uuid(UUID.randomUUID()).build()))
                .build();

        savedPost = new Post.Builder()
                .uuid(postUuid)
                .title("Заголовок")
                .content("Контент")
                .imageUrl(imageUrl)
                .tags(List.of(new Tag.Builder().uuid(UUID.randomUUID()).build()))
                .build();
    }

    @Test
    void save_shouldReturnPost() {
        when(postRepository.save(any())).thenReturn(postUuid);
        when(postRepository.get(postUuid)).thenReturn(PostDaoMapper.postToPostDao(savedPost));
        when(tagService.getAllBy(postUuid)).thenReturn(savedPost.getTags());
        when(commentService.getAllBy(postUuid)).thenReturn(savedPost.getComments());

        Post result = postService.save(post);

        assertNotNull(result);
        assertEquals(postUuid, result.getUuid());
        verify(postRepository, times(1)).save(any());
        verify(postRepository, times(1)).get(postUuid);
        verify(tagService, times(1)).batchUpdatePostTags(any(), any());
    }

    @Test
    void update_shouldReturnPost() {
        when(postRepository.get(postUuid)).thenReturn(PostDaoMapper.postToPostDao(savedPost));
        when(tagService.getAllBy(postUuid)).thenReturn(savedPost.getTags());
        when(commentService.getAllBy(postUuid)).thenReturn(savedPost.getComments());

        Post result = postService.update(savedPost);

        assertNotNull(result);
        assertEquals(postUuid, result.getUuid());
        verify(postRepository, times(1)).update(any());
        verify(tagService, times(1)).deleteAllBy(postUuid);
        verify(tagService, times(1)).batchUpdatePostTags(postUuid, List.of(savedPost.getTags().getFirst().getUuid()));
    }

    @Test
    void getAllByTag_shouldReturnPosts() {
        UUID tagUuid = UUID.randomUUID();
        when(postRepository.getAllBy(tagUuid)).thenReturn(List.of(PostDaoMapper.postToPostDao(savedPost)));
        when(tagService.getAllBy(postUuid)).thenReturn(savedPost.getTags());
        when(commentService.getAllBy(postUuid)).thenReturn(savedPost.getComments());

        List<PostPreview> result = postService.getAllByTag(tagUuid);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(postUuid, result.getFirst().getUuid());
        verify(postRepository, times(1)).getAllBy(tagUuid);
    }

    @Test
    void getPage_shouldReturnPostsWithPagination() {
        int page = 1;
        int size = 10;
        when(postRepository.getPage(page, size)).thenReturn(List.of(PostDaoMapper.postToPostDao(savedPost)));
        when(tagService.getAllBy(postUuid)).thenReturn(savedPost.getTags());
        when(commentService.getAllBy(postUuid)).thenReturn(savedPost.getComments());

        List<PostPreview> result = postService.getPage(page, size);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(postUuid, result.getFirst().getUuid());
        verify(postRepository, times(1)).getPage(page, size);
    }

    @Test
    void get_shouldReturnPost() {
        when(postRepository.get(postUuid)).thenReturn(PostDaoMapper.postToPostDao(savedPost));
        when(tagService.getAllBy(postUuid)).thenReturn(savedPost.getTags());
        when(commentService.getAllBy(postUuid)).thenReturn(savedPost.getComments());

        Post result = postService.get(postUuid);

        assertNotNull(result);
        assertEquals(postUuid, result.getUuid());
        verify(postRepository, times(1)).get(postUuid);
    }

    @Test
    void setLike_shouldCallRepository() {
        doNothing().when(postRepository).setLike(postUuid);

        postService.setLike(postUuid);

        verify(postRepository, times(1)).setLike(postUuid);
    }

    @Test
    void delete_shouldCallRepository() {
        doNothing().when(postRepository).deleteBy(postUuid);

        postService.delete(postUuid);

        verify(postRepository, times(1)).deleteBy(postUuid);
    }

    @Test
    void getTotal_shouldReturnTotalCount() {
        when(postRepository.getTotal()).thenReturn(1L);

        long result = postService.getTotal();

        assertEquals(1L, result);
        verify(postRepository, times(1)).getTotal();
    }
}