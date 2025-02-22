package ru.practicum.mapper.tag;

import ru.practicum.dto.tag.TagViewDto;
import ru.practicum.model.tag.Tag;

import java.util.List;

/**
 * Маппер тегов
 */
public class TagMapper {

    /**
     * Смаппить тег в DTO тега
     *
     * @param tag Тег
     * @return DTO тега
     */
    public static TagViewDto tagToTagViewDto(Tag tag) {
        return new TagViewDto.Builder()
                .uuid(tag.getUuid())
                .title(tag.getTitle())
                .build();
    }

    /**
     * Смаппить список тегов в список DTO тегов
     *
     * @param tagList Список тегов
     * @return Список DTO тегов
     */
    public static List<TagViewDto> tagsToTagViewDtoList(List<Tag> tagList) {
        return tagList.stream().map(TagMapper::tagToTagViewDto).toList();
    }
}
