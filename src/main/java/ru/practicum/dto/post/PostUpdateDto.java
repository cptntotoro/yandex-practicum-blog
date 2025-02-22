package ru.practicum.dto.post;

import java.net.URL;
import java.util.List;
import java.util.UUID;

/**
 * DTO поста для обновления
 */
public class PostUpdateDto extends PostBaseOutDto<UUID> {

    public PostUpdateDto() {
    }

    public PostUpdateDto(UUID uuid, String title, URL imageUrl, String content, List<UUID> tags) {
        super(uuid, title, imageUrl, content, tags);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder extends PostBaseOutDto.Builder<UUID, Builder> {
        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public PostUpdateDto build() {
            return new PostUpdateDto(getUuid(), title, imageUrl, content, tags);
        }
    }
}