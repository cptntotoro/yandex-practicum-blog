package ru.practicum.dto.post;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Абстрактное базовое DTO постов
 */
public abstract class PostBaseDto<T> {
    protected String title;
    protected URL imageUrl;
    protected String content;
    protected List<T> tags = new ArrayList<>();

    protected PostBaseDto() {
    }

    protected PostBaseDto(String title, URL imageUrl, String content, List<T> tags) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.content = content;
        this.tags = tags;
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

    public List<T> getTags() {
        return tags;
    }

    protected static abstract class Builder<T, B extends Builder<T, B>> {
        protected String title;
        protected URL imageUrl;
        protected String content;
        protected List<T> tags = new ArrayList<>();

        public B title(String title) {
            this.title = title;
            return self();
        }

        public B imageUrl(URL imageUrl) {
            this.imageUrl = imageUrl;
            return self();
        }

        public B content(String content) {
            this.content = "<p>" + content.replace("\n", "</p><p>");
            return self();
        }

        public B tags(List<T> tags) {
            this.tags = tags;
            return self();
        }

        protected abstract B self();

        public abstract PostBaseDto<T> build();
    }
}