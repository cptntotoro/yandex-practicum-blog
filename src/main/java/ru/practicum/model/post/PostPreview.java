package ru.practicum.model.post;

import ru.practicum.model.comment.Comment;
import ru.practicum.model.tag.Tag;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Превью поста
 */
public class PostPreview extends BasePost {
    private final String preview;

    private PostPreview(UUID uuid, String title, URL imageUrl, String preview, List<Tag> tags, Integer likesCounter, List<Comment> comments, LocalDateTime dateTime) {
        super(uuid, title, imageUrl, tags, likesCounter, comments, dateTime);
        this.preview = preview;
    }

    public String getPreview() {
        return preview;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder extends BasePost.Builder<Builder> {
        private String preview;

        public Builder() {
        }

        public Builder preview(String preview) {
            this.preview = preview;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public PostPreview build() {
            return new PostPreview(uuid, title, imageUrl, preview, tags, likesCounter, comments, dateTime);
        }
    }
}