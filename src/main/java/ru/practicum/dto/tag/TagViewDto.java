package ru.practicum.dto.tag;

import java.util.UUID;

/**
 * DTO тега
 */
public record TagViewDto(UUID uuid, String title) {

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private UUID uuid;
        private String title;

        public Builder() {
        }

        public Builder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        protected Builder self() {
            return this;
        }

        public TagViewDto build() {
            return new TagViewDto(uuid, title);
        }
    }
}