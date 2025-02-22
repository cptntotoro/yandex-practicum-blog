package ru.practicum.mapper.tag;

import ru.practicum.dao.tag.TagDao;
import ru.practicum.model.tag.Tag;

import java.util.List;

/**
 * Маппер тегов для БД
 */
public class TagDaoMapper {
    /**
     * Смаппить список тегов для БД в список тегов
     *
     * @param tagDaoList Список тегов для БД
     * @return Список тегов
     */
    public static List<Tag> tagDaoListToTagList(List<TagDao> tagDaoList) {
        return tagDaoList.stream()
                .map(TagDaoMapper::tagDaoToTag)
                .toList();
    }

    /**
     * Смаппить тег для БД в тег
     *
     * @param tagDao Тег для БД
     * @return Тег
     */
    public static Tag tagDaoToTag(TagDao tagDao) {
        return new Tag.Builder()
                .uuid(tagDao.getUuid())
                .title(tagDao.getTitle())
                .build();
    }

    /**
     * Смаппить тег в тег для БД
     *
     * @param tag Тег
     * @return Тег для БД
     */
    public static TagDao tagToTagDao(Tag tag) {
        return new TagDao.Builder()
                .uuid(tag.getUuid())
                .title(tag.getTitle())
                .build();
    }
}
