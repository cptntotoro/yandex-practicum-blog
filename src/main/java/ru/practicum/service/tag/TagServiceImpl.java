package ru.practicum.service.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.exception.tag.InvalidTagException;
import ru.practicum.mapper.tag.TagDaoMapper;
import ru.practicum.model.tag.Tag;
import ru.practicum.repository.tag.TagRepository;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Сервис управления тегами
 */
@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private static final Pattern TAG_NAME_PATTERN = Pattern.compile("^[а-яА-Яa-zA-Z\\s]+$");

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> getAll() {
        return TagDaoMapper.tagDaoListToTagList(tagRepository.getAll());
    }

    @Override
    public Tag save(Tag tag) {
        validateTag(tag);
        UUID tagUuid = tagRepository.save(TagDaoMapper.tagToTagDao(tag));
        return getTagByUuid(tagUuid);
    }

    @Override
    public List<Tag> getAllBy(UUID postUuid) {
        return TagDaoMapper.tagDaoListToTagList(tagRepository.getAllBy(postUuid));
    }

    @Override
    public void deleteAllBy(UUID postUuid) {
        tagRepository.deleteAllBy(postUuid);
    }

    @Override
    public void batchUpdatePostTags(UUID postUuid, List<UUID> postTagUuids) {
        tagRepository.batchUpdatePostTags(postUuid, postTagUuids);
    }

    @Override
    public void delete(UUID uuid) {
        tagRepository.deleteBy(uuid);
    }

    /**
     * Получает тег по UUID.
     *
     * @param tagUuid UUID тега
     * @return Тег
     */
    private Tag getTagByUuid(UUID tagUuid) {
        return TagDaoMapper.tagDaoToTag(tagRepository.get(tagUuid));
    }

    /**
     * Валидирует тег.
     *
     * @param tag Тег
     * @throws InvalidTagException если тег невалиден
     */
    private void validateTag(Tag tag) {
        if (tag == null) {
            throw new InvalidTagException("Тег не может быть пустым");
        }

        if (tag.getTitle() == null || tag.getTitle().trim().isEmpty()) {
            throw new InvalidTagException("Название тега не может быть пустым");
        }

        if (!TAG_NAME_PATTERN.matcher(tag.getTitle()).matches()) {
            throw new InvalidTagException("Название тега может содержать только русские и английские буквы и пробелы");
        }
    }
}