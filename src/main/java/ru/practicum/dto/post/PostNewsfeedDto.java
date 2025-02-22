package ru.practicum.dto.post;

import ru.practicum.model.tag.Tag;

import java.net.URL;
import java.util.List;
import java.util.UUID;

/**
 * DTO поста для ленты постов (без content)
 */
public class PostNewsfeedDto extends PostBasePreviewDto<Tag> {
    private final UUID uuid;
    private final Integer commentsCounter;
    private final Integer likesCounter;
    private final String preview;

    public PostNewsfeedDto(UUID uuid, String title, URL imageUrl, List<Tag> tags,
                           Integer commentsCounter, Integer likesCounter, String preview) {
        super(title, imageUrl, tags);
        this.uuid = uuid;
        this.commentsCounter = commentsCounter;
        this.likesCounter = likesCounter;
        this.preview = preview;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Integer getCommentsCounter() {
        return commentsCounter;
    }

    public Integer getLikesCounter() {
        return likesCounter;
    }

    public String getPreview() {
        return preview;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder extends PostBasePreviewDto.Builder<Tag, Builder> {
        private UUID uuid;
        private Integer commentsCounter;
        private Integer likesCounter;
        private String preview;

        public Builder() {
        }

        @Override
        protected Builder self() {
            return this;
        }

        public Builder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder commentsCounter(Integer commentsCounter) {
            this.commentsCounter = commentsCounter;
            return this;
        }

        public Builder likesCounter(Integer likesCounter) {
            this.likesCounter = likesCounter;
            return this;
        }

        public Builder preview(String preview) {
            this.preview = "<p>" + preview.replace("\n", "</p><p>");
            return this;
        }

        @Override
        public PostNewsfeedDto build() {
            return new PostNewsfeedDto(uuid, title, imageUrl, tags, commentsCounter, likesCounter, preview);
        }
    }
}