package ru.practicum.dto.comment;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Полный комментарий
 */
public class CommentViewDto {
    private UUID uuid;
    private String content;
    @DateTimeFormat(pattern = "dd-MMM-YYYY HH:mm")
    private LocalDateTime dateTime;

    public CommentViewDto() {
    }

    private CommentViewDto(UUID uuid, String content, LocalDateTime dateTime) {
        this.uuid = uuid;
        this.content = content;
        this.dateTime = dateTime;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private UUID uuid;
        private String content;
        private LocalDateTime dateTime;

        public Builder() {
        }

        public Builder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder dateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public CommentViewDto build() {
            return new CommentViewDto(uuid, content, dateTime);
        }
    }
}