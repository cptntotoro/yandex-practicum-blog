package ru.practicum.dto.post;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Базовый класс для постов без content
 */
public abstract class PostBasePreviewDto<T> {
    protected String title;
    protected URL imageUrl;
    protected List<T> tags = new ArrayList<>();

    protected PostBasePreviewDto() {
    }

    protected PostBasePreviewDto(String title, URL imageUrl, List<T> tags) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public URL getImageUrl() {
        return imageUrl;
    }

    public List<T> getTags() {
        return tags;
    }

    protected static abstract class Builder<T, B extends Builder<T, B>> {
        protected String title;
        protected URL imageUrl;
        protected List<T> tags = new ArrayList<>();

        public B title(String title) {
            this.title = title;
            return self();
        }

        public B imageUrl(URL imageUrl) {
            this.imageUrl = imageUrl;
            return self();
        }

        public B tags(List<T> tags) {
            this.tags = tags;
            return self();
        }

        protected abstract B self();

        public abstract PostBasePreviewDto<T> build();
    }
}