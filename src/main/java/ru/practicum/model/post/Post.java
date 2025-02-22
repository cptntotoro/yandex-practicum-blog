package ru.practicum.model.post;

import ru.practicum.model.comment.Comment;
import ru.practicum.model.tag.Tag;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Полный пост
 */
public class Post extends BasePost {
    private final String content;

    private Post(UUID uuid, String title, URL imageUrl, String content, List<Tag> tags, Integer likesCounter, List<Comment> comments, LocalDateTime dateTime) {
        super(uuid, title, imageUrl, tags, likesCounter, comments, dateTime);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder extends BasePost.Builder<Builder> {
        private String content;

        public Builder() {
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Post build() {
            return new Post(uuid, title, imageUrl, content, tags, likesCounter, comments, dateTime);
        }
    }
}