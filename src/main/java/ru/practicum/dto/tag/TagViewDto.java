package ru.practicum.dto.tag;

import java.util.UUID;

/**
 * DTO тега
 */
public class TagViewDto extends TagBaseDto {
    private final UUID uuid;

    private TagViewDto(UUID uuid, String title) {
        super(title);
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder extends TagBaseDto.Builder<Builder> {
        private UUID uuid;

        public Builder() {
        }

        public Builder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        public TagViewDto build() {
            return new TagViewDto(uuid, title);
        }
    }
}