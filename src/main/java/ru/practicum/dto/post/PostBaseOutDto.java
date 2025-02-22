package ru.practicum.dto.post;

import java.net.URL;
import java.util.List;
import java.util.UUID;

/**
 * Абстрактное базовое отдаваемое DTO постов с UUID
 */
public abstract class PostBaseOutDto<T> extends PostBaseDto<T> {
    private UUID uuid;

    protected PostBaseOutDto() {
    }

    protected PostBaseOutDto(UUID uuid, String title, URL imageUrl, String content, List<T> tags) {
        super(title, imageUrl, content, tags);
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    protected static abstract class Builder<T, B extends Builder<T, B>> extends PostBaseDto.Builder<T, B> {
        private UUID uuid;

        public B uuid(UUID uuid) {
            this.uuid = uuid;
            return self();
        }

        protected UUID getUuid() {
            return uuid;
        }
    }
}