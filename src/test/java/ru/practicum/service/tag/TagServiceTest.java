package ru.practicum.service.tag;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.dao.tag.TagDao;
import ru.practicum.exception.tag.InvalidTagException;
import ru.practicum.model.tag.Tag;
import ru.practicum.repository.tag.TagRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class TagServiceTest {
    private static final String TAG_TITLE = "Тег Один";
    private static final String INVALID_TAG_TITLE = "Тег 1";

    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private TagRepository tagRepository;

    private UUID tagUuid;
    private Tag tag;
    private TagDao tagDao;

    @BeforeEach
    void setUp() {
        tagUuid = UUID.randomUUID();
        tag = buildTag(TAG_TITLE);
        tagDao = buildTagDao(tagUuid, TAG_TITLE);
    }

    @Test
    void save_shouldReturnTag() {
        when(tagRepository.save(any())).thenReturn(tagUuid);
        when(tagRepository.get(tagUuid)).thenReturn(tagDao);

        Tag result = tagService.save(tag);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(tagUuid, result.getUuid()),
                () -> assertEquals(TAG_TITLE, result.getTitle())
        );

        verify(tagRepository).save(any());
        verify(tagRepository).get(tagUuid);
    }

    @Test
    void save_shouldThrowInvalidTagException() {
        Tag invalidTag = buildTag(tagUuid, INVALID_TAG_TITLE);

        assertThrows(InvalidTagException.class, () -> tagService.save(invalidTag));
        verifyNoInteractions(tagRepository);
    }

    @Test
    void getAll_shouldReturnTags() {
        when(tagRepository.getAll()).thenReturn(List.of(tagDao));

        List<Tag> result = tagService.getAll();

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.size()),
                () -> assertEquals(tagUuid, result.getFirst().getUuid())
        );

        verify(tagRepository).getAll();
    }

    @Test
    void delete_shouldDeleteTag() {
        tagService.delete(tagUuid);
        verify(tagRepository).deleteBy(tagUuid);
    }

    @Test
    void getAllBy_shouldReturnTagsByPostUuid() {
        UUID postUuid = UUID.randomUUID();
        when(tagRepository.getAllBy(postUuid)).thenReturn(List.of(tagDao));

        List<Tag> result = tagService.getAllBy(postUuid);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.size()),
                () -> assertEquals(tagUuid, result.getFirst().getUuid())
        );

        verify(tagRepository).getAllBy(postUuid);
    }

    @Test
    void deleteAllBy_shouldDeleteTagsByPostUuid() {
        UUID postUuid = UUID.randomUUID();
        tagService.deleteAllBy(postUuid);
        verify(tagRepository).deleteAllBy(postUuid);
    }

    @Test
    void batchUpdatePostTags_shouldUpdatePostTags() {
        UUID postUuid = UUID.randomUUID();
        List<UUID> tagUuids = List.of(tagUuid);

        tagService.batchUpdatePostTags(postUuid, tagUuids);
        verify(tagRepository).batchUpdatePostTags(postUuid, tagUuids);
    }

    private Tag buildTag(String title) {
        return new Tag().newBuilder()
                .title(title)
                .build();
    }

    private Tag buildTag(UUID uuid, String title) {
        return new Tag().newBuilder()
                .uuid(uuid)
                .title(title)
                .build();
    }

    private TagDao buildTagDao(UUID uuid, String title) {
        return new TagDao.Builder()
                .uuid(uuid)
                .title(title)
                .build();
    }
}