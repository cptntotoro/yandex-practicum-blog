package ru.practicum.dto.comment;

/**
 * Комментарий для обновления
 */
public class CommentUpdateDto extends CommentBaseDto {

    public CommentUpdateDto() {
    }

    public CommentUpdateDto(String content) {
        super(content);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String content;

        public Builder() {
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public CommentUpdateDto build() {
            return new CommentUpdateDto(content);
        }
    }
}