package ru.practicum.service.tag;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.dao.tag.TagDao;
import ru.practicum.exception.tag.InvalidTagException;
import ru.practicum.model.tag.Tag;
import ru.practicum.repository.tag.TagRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class TagServiceTest {
    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private TagRepository tagRepository;

    private Tag tag;
    private TagDao tagDao;
    private final UUID tagUUID = UUID.randomUUID();

    @BeforeEach
    void setup() {
        tag = new Tag().newBuilder()
                .title("Тег Один")
                .build();

        tagDao = new TagDao.Builder()
                .uuid(tagUUID)
                .title("Тег Один")
                .build();
    }

    @Test
    void save_shouldReturnTag() {
        Mockito.when(tagRepository.save(any()))
                .thenReturn(tagUUID);

        Mockito.when(tagRepository.get(tagUUID))
                .thenReturn(tagDao);

        Tag savedTag = tagService.save(tag);

        assertNotNull(savedTag);
        assertEquals(tagUUID, savedTag.getUuid());
        verify(tagRepository, times(1)).save(any());
        verify(tagRepository, times(1)).get(tagUUID);
    }

    @Test
    void save_shouldThrowInvalidTagException() {
        Tag invalidTag = new Tag().newBuilder()
                .uuid(tagUUID)
                .title("Тег 1")
                .build();

        assertThrows(InvalidTagException.class, () -> tagService.save(invalidTag));
        verify(tagRepository, never()).save(any());
    }

    @Test
    void getAll_shouldReturnTags() {
        Mockito.when(tagRepository.getAll())
                .thenReturn(List.of(tagDao));

        List<Tag> tags = tagService.getAll();

        assertNotNull(tags);
        assertFalse(tags.isEmpty());
        assertEquals(1, tags.size());
        verify(tagRepository, times(1)).getAll();
    }

    @Test
    void delete_shouldDeleteTag() {
        Mockito.doNothing().when(tagRepository).deleteBy(tagUUID);

        tagService.delete(tagUUID);

        verify(tagRepository, times(1)).deleteBy(tagUUID);
    }

    @Test
    void getAllBy_shouldReturnTagsByPostUuid() {
        UUID postUuid = UUID.randomUUID();
        Mockito.when(tagRepository.getAllBy(postUuid))
                .thenReturn(List.of(tagDao));

        List<Tag> tags = tagService.getAllBy(postUuid);

        assertNotNull(tags);
        assertFalse(tags.isEmpty());
        assertEquals(1, tags.size());
        verify(tagRepository, times(1)).getAllBy(postUuid);
    }

    @Test
    void deleteAllBy_shouldDeleteTagsByPostUuid() {
        UUID postUuid = UUID.randomUUID();
        Mockito.doNothing().when(tagRepository).deleteAllBy(postUuid);

        tagService.deleteAllBy(postUuid);

        verify(tagRepository, times(1)).deleteAllBy(postUuid);
    }

    @Test
    void batchUpdatePostTags_shouldUpdatePostTags() {
        UUID postUuid = UUID.randomUUID();
        List<UUID> tagUuids = List.of(tagUUID);

        Mockito.doNothing().when(tagRepository).batchUpdatePostTags(postUuid, tagUuids);

        tagService.batchUpdatePostTags(postUuid, tagUuids);

        verify(tagRepository, times(1)).batchUpdatePostTags(postUuid, tagUuids);
    }
}