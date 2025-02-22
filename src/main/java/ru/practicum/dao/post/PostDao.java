package ru.practicum.dao.post;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.UUID;

public class PostDao {
    /**
     * Идентификтор
     */
    private final UUID uuid;
    /**
     * Название
     */
    private final String title;
    /**
     * URL картинки
     */
    private final URL imageUrl;
    /**
     * Содержание поста
     */
    private final String content;
    /**
     * Краткое содержание поста
     */
    private final String preview;
    /**
     * Счетчик лайков
     */
    private final Integer likesCounter;
    /**
     * Дата и время создания
     */
    private final LocalDateTime dateTime;

    private PostDao(UUID uuid, String title, URL imageUrl, String content, String preview, Integer likesCounter, LocalDateTime dateTime) {
        this.uuid = uuid;
        this.title = title;
        this.imageUrl = imageUrl;
        this.content = content;
        this.preview = preview;
        this.likesCounter = likesCounter;
        this.dateTime = dateTime;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getTitle() {
        return title;
    }

    public URL getImageUrl() {
        return imageUrl;
    }

    public String getContent() {
        return content;
    }

    public String getPreview() {
        return preview;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Integer getLikesCounter() {
        return likesCounter;
    }

    public Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private UUID uuid;
        private String title;
        private URL imageUrl;
        private String content;
        private String preview;
        private Integer likesCounter;
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
         * Установить название
         * @param title название
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * Установить URL картинки
         * @param imageUrl URL картинки
         */
        public Builder imageUrl(URL imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        /**
         * Установить содержание
         * @param content содержание
         */
        public Builder content(String content) {
            this.content = content;
            return this;
        }

        /**
         * Установить краткое содержание
         * @param preview Краткое содержание
         */
        public Builder preview(String preview) {
            this.preview = preview;
            return this;
        }

        /**
         * Установить счетчик лайков
         * @param likesCounter счетчик лайков
         */
        public Builder likesCounter(Integer likesCounter) {
            this.likesCounter = likesCounter;
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

        public PostDao build() {
            return new PostDao(uuid, title, imageUrl, content, preview, likesCounter, dateTime);
        }
    }
}
