package ru.practicum.dto.comment;

/**
 * Комментарий для добавления
 */
public class CommentAddDto extends CommentBaseDto {

    public CommentAddDto() {
    }

    public CommentAddDto(String content) {
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

        public CommentAddDto build() {
            return new CommentAddDto(content);
        }
    }
}