package ru.practicum.dto.comment;

/**
 * Базовое DTO для комментариев
 */
public abstract class CommentBaseDto {
    protected String content;

    protected CommentBaseDto() {
    }

    protected CommentBaseDto(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}