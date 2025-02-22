package ru.practicum.dao.tag;

import java.util.UUID;

public class TagDao {
    /**
     * Идентификатор
     */
    private final UUID uuid;
    /**
     * Название
     */
    private final String title;

    private TagDao(UUID uuid, String title) {
        this.uuid = uuid;
        this.title = title;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getTitle() {
        return title;
    }

    public Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        /**
         * Идентификатор
         */
        private UUID uuid;
        /**
         * Название
         */
        private String title;

        public Builder() {
        }

        /**
         * Установить идентификатор
         * @param uuid Идентификатор
         */
        public Builder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        /**
         * Установить название
         * @param title название
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public TagDao build() {
            return new TagDao(uuid, title);
        }

    }
}
