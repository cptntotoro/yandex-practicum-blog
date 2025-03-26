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
class PostServiceTest {
    private static final String POST_TITLE = "Заголовок";
    private static final String POST_CONTENT = "Контент";
    private static final String IMAGE_URL = "https://upload.wikimedia.org/wikipedia/commons/4/4d/Cat_November_2010-1a.jpg";
    private static final int PAGE = 1;
    private static final int SIZE = 10;
    private static final long TOTAL_COUNT = 1L;

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
    private UUID postUuid;
    private URL imageUrl;

    @BeforeEach
    void setUp() throws URISyntaxException, MalformedURLException {
        postUuid = UUID.randomUUID();
        imageUrl = new URI(IMAGE_URL).toURL();

        post = buildPost(null, POST_TITLE, POST_CONTENT, imageUrl);
        savedPost = buildPost(postUuid, POST_TITLE, POST_CONTENT, imageUrl);
    }

    @Test
    void save_shouldReturnPost() {
        when(postRepository.save(any())).thenReturn(postUuid);
        when(postRepository.get(postUuid)).thenReturn(PostDaoMapper.postToPostDao(savedPost));
        when(tagService.getAllBy(postUuid)).thenReturn(savedPost.getTags());
        when(commentService.getAllBy(postUuid)).thenReturn(savedPost.getComments());

        Post result = postService.save(post);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(postUuid, result.getUuid())
        );

        verify(postRepository).save(any());
        verify(postRepository).get(postUuid);
        verify(tagService).batchUpdatePostTags(any(), any());
    }

    @Test
    void update_shouldReturnPost() {
        when(postRepository.get(postUuid)).thenReturn(PostDaoMapper.postToPostDao(savedPost));
        when(tagService.getAllBy(postUuid)).thenReturn(savedPost.getTags());
        when(commentService.getAllBy(postUuid)).thenReturn(savedPost.getComments());

        Post result = postService.update(savedPost);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(postUuid, result.getUuid())
        );

        verify(postRepository).update(any());
        verify(tagService).deleteAllBy(postUuid);
        verify(tagService).batchUpdatePostTags(eq(postUuid), anyList());
    }

    @Test
    void checkIsExist_shouldNotThrowExceptionWhenPostExists() {
        when(postRepository.isExist(postUuid)).thenReturn(true);

        assertDoesNotThrow(() -> postService.checkIsExist(postUuid));
        verify(postRepository).isExist(postUuid);
    }

    @Test
    void getAllByTag_shouldReturnPosts() {
        UUID tagUuid = UUID.randomUUID();
        when(postRepository.getAllBy(tagUuid)).thenReturn(List.of(PostDaoMapper.postToPostDao(savedPost)));
        when(tagService.getAllBy(postUuid)).thenReturn(savedPost.getTags());
        when(commentService.getAllBy(postUuid)).thenReturn(savedPost.getComments());

        List<PostPreview> result = postService.getAllByTag(tagUuid);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals(postUuid, result.getFirst().getUuid())
        );

        verify(postRepository).getAllBy(tagUuid);
    }

    @Test
    void getPage_shouldReturnPostsWithPagination() {
        when(postRepository.getPage(PAGE, SIZE)).thenReturn(List.of(PostDaoMapper.postToPostDao(savedPost)));
        when(tagService.getAllBy(postUuid)).thenReturn(savedPost.getTags());
        when(commentService.getAllBy(postUuid)).thenReturn(savedPost.getComments());

        List<PostPreview> result = postService.getPage(PAGE, SIZE);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals(postUuid, result.getFirst().getUuid())
        );

        verify(postRepository).getPage(PAGE, SIZE);
    }

    @Test
    void get_shouldReturnPost() {
        when(postRepository.get(postUuid)).thenReturn(PostDaoMapper.postToPostDao(savedPost));
        when(tagService.getAllBy(postUuid)).thenReturn(savedPost.getTags());
        when(commentService.getAllBy(postUuid)).thenReturn(savedPost.getComments());

        Post result = postService.get(postUuid);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(postUuid, result.getUuid())
        );

        verify(postRepository).get(postUuid);
    }

    @Test
    void setLike_shouldCallRepository() {
        postService.setLike(postUuid);
        verify(postRepository).setLike(postUuid);
    }

    @Test
    void delete_shouldCallRepository() {
        postService.delete(postUuid);
        verify(postRepository).deleteBy(postUuid);
    }

    @Test
    void getTotal_shouldReturnTotalCount() {
        when(postRepository.getTotal()).thenReturn(TOTAL_COUNT);

        assertEquals(TOTAL_COUNT, postService.getTotal());
        verify(postRepository).getTotal();
    }

    private Post buildPost(UUID uuid, String title, String content, URL imageUrl) {
        return new Post.Builder()
                .uuid(uuid)
                .title(title)
                .content(content)
                .imageUrl(imageUrl)
                .tags(List.of(new Tag.Builder().uuid(UUID.randomUUID()).build()))
                .build();
    }
}