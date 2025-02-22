package ru.practicum.model.tag;

import java.util.UUID;

/**
 * Тег
 */
public class Tag {
    /**
     * Идентификатор
     */
    private UUID uuid;
    /**
     * Название
     */
    private String title;

    public Tag() {
    }

    private Tag(UUID uuid, String title) {
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
        /**
         * Список идентификаторов постов
         */

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

        public Tag build() {
            return new Tag(uuid, title);
        }

    }
}
