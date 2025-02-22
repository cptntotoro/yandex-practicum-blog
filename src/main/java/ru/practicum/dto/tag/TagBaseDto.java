package ru.practicum.dto.tag;

/**
 * Базовое DTO для тегов
 */
public abstract class TagBaseDto {
    protected String title;

    protected TagBaseDto(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    protected static abstract class Builder<B extends Builder<B>> {
        protected String title;

        public B title(String title) {
            this.title = title;
            return self();
        }

        protected abstract B self();
    }
}