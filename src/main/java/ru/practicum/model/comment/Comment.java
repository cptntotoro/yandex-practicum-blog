package ru.practicum.model.comment;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Комментарий
 */
public class Comment {
    /**
     * Идентификатор
     */
    private final UUID uuid;
    /**
     * Идентификатор поста
     */
    private final UUID postUuid;
    /**
     * Содержание комментария
     */
    private final String content;
    /**
     * Дата и время создания комментария
     */
    private final LocalDateTime dateTime;

    private Comment(UUID uuid, UUID postUuid, String content, LocalDateTime dateTime) {
        this.uuid = uuid;
        this.postUuid = postUuid;
        this.content = content;
        this.dateTime = dateTime;
    }

    public UUID getUuid() {
        return uuid;
    }

    public UUID getPostUuid() {
        return postUuid;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private UUID uuid;
        private UUID postUuid;
        private String content;
        private LocalDateTime dateTime;

        public Builder() {
        }

        /**
         * Установить идентификатор
         * @param uuid идентификатор
         */
        public Builder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        /**
         * Установить идентификатор поста
         * @param postUuid идентификатор поста
         */
        public Builder postUuid(UUID postUuid) {
            this.postUuid = postUuid;
            return this;
        }

        /**
         * Установить содержание комментария
         * @param content содержание комментария
         */
        public Builder content(String content) {
            this.content = content;
            return this;
        }

        /**
         * Установить дату и время создания
         * @param dateTime Дата и время создания
         */
        public Builder dateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public Comment build() {
            return new Comment(uuid, postUuid, content, dateTime);
        }

    }
}


