package ru.practicum.dto.post;

import ru.practicum.dto.comment.CommentViewDto;
import ru.practicum.dto.tag.TagViewDto;

import java.net.URL;
import java.util.List;
import java.util.UUID;

/**
 * DTO полного поста в отдельной странице
 */
public class PostViewDto extends PostBaseOutDto<TagViewDto> {
    private final Integer likesCounter;
    private final List<CommentViewDto> comments;

    public PostViewDto(UUID uuid, String title, URL imageUrl, String content,
                       List<TagViewDto> tags, Integer likesCounter, List<CommentViewDto> comments) {
        super(uuid, title, imageUrl, content, tags);
        this.likesCounter = likesCounter;
        this.comments = comments;
    }

    public Integer getLikesCounter() {
        return likesCounter;
    }

    public List<CommentViewDto> getComments() {
        return comments;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder extends PostBaseOutDto.Builder<TagViewDto, Builder> {
        private Integer likesCounter;
        private List<CommentViewDto> comments;

        public Builder() {
        }

        @Override
        protected Builder self() {
            return this;
        }

        public Builder likesCounter(Integer likesCounter) {
            this.likesCounter = likesCounter;
            return this;
        }

        public Builder comments(List<CommentViewDto> comments) {
            this.comments = comments;
            return this;
        }

        @Override
        public PostViewDto build() {
            return new PostViewDto(getUuid(), title, imageUrl, content, tags, likesCounter, comments);
        }
    }
}