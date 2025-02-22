package ru.practicum.model.post;

import ru.practicum.model.comment.Comment;
import ru.practicum.model.tag.Tag;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Базовый класс для постов
 */
public abstract class BasePost {
    private UUID uuid;
    private String title;
    private URL imageUrl;
    private List<Tag> tags = new ArrayList<>();
    private Integer likesCounter;
    private List<Comment> comments = new ArrayList<>();
    private LocalDateTime dateTime;

    protected BasePost() {
    }

    protected BasePost(UUID uuid, String title, URL imageUrl, List<Tag> tags, Integer likesCounter, List<Comment> comments, LocalDateTime dateTime) {
        this.uuid = uuid;
        this.title = title;
        this.imageUrl = imageUrl;
        this.tags = tags;
        this.likesCounter = likesCounter;
        this.comments = comments;
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

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Integer getLikesCounter() {
        return likesCounter;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Базовый строитель
     */
    protected static abstract class Builder<T extends Builder<T>> {
        protected UUID uuid;
        protected String title;
        protected URL imageUrl;
        protected List<Tag> tags = new ArrayList<>();
        protected Integer likesCounter;
        protected List<Comment> comments = new ArrayList<>();
        protected LocalDateTime dateTime;

        protected Builder() {
        }

        public T uuid(UUID uuid) {
            this.uuid = uuid;
            return self();
        }

        public T title(String title) {
            this.title = title;
            return self();
        }

        public T imageUrl(URL imageUrl) {
            this.imageUrl = imageUrl;
            return self();
        }

        public T tags(List<Tag> tags) {
            this.tags = tags;
            return self();
        }

        public T likesCounter(Integer likesCounter) {
            this.likesCounter = likesCounter;
            return self();
        }

        public T comments(List<Comment> comments) {
            this.comments = comments;
            return self();
        }

        public T dateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
            return self();
        }

        protected abstract T self();

        public abstract BasePost build();
    }
}