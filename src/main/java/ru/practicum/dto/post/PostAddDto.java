package ru.practicum.dto.post;

import java.net.URL;
import java.util.List;
import java.util.UUID;

/**
 * DTO поста на добавление
 */
public class PostAddDto extends PostBaseDto<UUID> {

    public PostAddDto() {
    }

    public PostAddDto(String title, URL imageUrl, String content, List<UUID> tags) {
        super(title, imageUrl, content, tags);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder extends PostBaseDto.Builder<UUID, Builder> {
        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public PostAddDto build() {
            return new PostAddDto(title, imageUrl, content, tags);
        }
    }
}