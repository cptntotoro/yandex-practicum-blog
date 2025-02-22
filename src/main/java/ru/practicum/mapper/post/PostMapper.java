package ru.practicum.mapper.post;

import ru.practicum.dto.post.PostUpdateDto;
import ru.practicum.mapper.comment.CommentMapper;
import ru.practicum.mapper.tag.TagMapper;
import ru.practicum.model.post.Post;
import ru.practicum.dto.post.PostAddDto;
import ru.practicum.dto.post.PostNewsfeedDto;
import ru.practicum.dto.post.PostViewDto;
import ru.practicum.model.post.PostPreview;
import ru.practicum.model.tag.Tag;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Маппер постов
 */
public class PostMapper {
    /**
     * Смаппить пост на добавление в пост
     *
     * @param postAddDto Пост на добавление
     * @return Пост
     */
    public static Post postAddDtoToPost(PostAddDto postAddDto) {
        List<UUID> tagUuids = postAddDto.getTags();
        return new Post.Builder()
                .title(postAddDto.getTitle())
                .content(postAddDto.getContent())
                .imageUrl(postAddDto.getImageUrl())
                .tags(tagUuids != null ? tagUuids.stream()
                        .map(uuid -> new Tag.Builder().uuid(uuid).build())
                        .collect(Collectors.toList()) : null)
                .build();
    }

    /**
     * Смаппить пост в пост для ленты постов
     *
     * @param post Пост
     * @return Пост
     */
    public static PostNewsfeedDto postToPostNewsfeedDto(PostPreview post) {
        return new PostNewsfeedDto.Builder()
                .uuid(post.getUuid())
                .title(post.getTitle())
                .imageUrl(post.getImageUrl())
                .preview(post.getPreview())
                .commentsCounter(post.getComments().size())
                .tags(post.getTags())
                .likesCounter(post.getLikesCounter())
                .build();
    }

    /**
     * Смаппить пост в DTO пост
     *
     * @param post Пост
     * @return DTO пост
     */
    public static PostViewDto postToPostViewDto(Post post) {
        return new PostViewDto.Builder()
                .uuid(post.getUuid())
                .title(post.getTitle())
                .imageUrl(post.getImageUrl())
                .content(post.getContent())
                .tags(TagMapper.tagsToTagViewDtoList(post.getTags()))
                .likesCounter(post.getLikesCounter())
                .comments(CommentMapper.commentsToCommentViewDtoList(post.getComments()))
                .build();
    }

    /**
     * Смаппить список постов в список DTO постов
     * @param postList Список постов
     * @return Список DTO постов
     */
    public static List<PostNewsfeedDto> postsToPostNewsfeedDtoList(List<PostPreview> postList) {
        return postList.stream()
                .map(PostMapper::postToPostNewsfeedDto)
                .toList();
    }

    /**
     * Смаппить пост для обновления в пост
     * @param postUuid Идентификатор поста
     * @param postUpdateDto Пост для обновления
     * @return Пост
     */
    public static Post postUpdateDtoToPost(UUID postUuid, PostUpdateDto postUpdateDto) {
        List<UUID> tagUuids = postUpdateDto.getTags();
        return new Post.Builder()
                .uuid(postUuid)
                .title(postUpdateDto.getTitle())
                .imageUrl(postUpdateDto.getImageUrl())
                .content(postUpdateDto.getContent())
                .tags(tagUuids != null ? tagUuids.stream()
                        .map(uuid -> new Tag.Builder().uuid(uuid).build())
                        .collect(Collectors.toList()) : null)
                .build();
    }
}
