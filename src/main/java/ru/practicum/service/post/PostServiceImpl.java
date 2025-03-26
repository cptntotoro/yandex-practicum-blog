package ru.practicum.service.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.post.PostNotFoundException;
import ru.practicum.mapper.post.PostDaoMapper;
import ru.practicum.model.post.BasePost;
import ru.practicum.model.post.Post;
import ru.practicum.model.post.PostPreview;
import ru.practicum.model.tag.Tag;
import ru.practicum.repository.post.PostRepository;
import ru.practicum.service.comment.CommentService;
import ru.practicum.service.tag.TagService;

import java.util.List;
import java.util.UUID;

/**
 * Сервис управления постами
 */
@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CommentService commentService;
    private final TagService tagService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, CommentService commentService, TagService tagService) {
        this.postRepository = postRepository;
        this.commentService = commentService;
        this.tagService = tagService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostPreview> getAllByTag(UUID tagUuid) {
        return enrichPostsWithTagsAndComments(PostDaoMapper.postDaoListToPostPreviewList(postRepository.getAllBy(tagUuid)));
    }

    @Override
    @Transactional
    public void setLike(UUID postUuid) {
        postRepository.setLike(postUuid);
    }

    @Override
    @Transactional
    public Post update(Post post) {
        postRepository.update(PostDaoMapper.postToPostDao(post));
        updatePostTags(post.getUuid(), post.getTags(), true);
        return get(post.getUuid());
    }

    @Override
    @Transactional(readOnly = true)
    public Post get(UUID postUuid) {
        Post post = PostDaoMapper.postDaoToPost(postRepository.get(postUuid));
        return enrichPostWithTagsAndComments(post);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostPreview> getPage(int page, int size) {
        return enrichPostsWithTagsAndComments(PostDaoMapper.postDaoListToPostPreviewList(postRepository.getPage(page, size)));
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotal() {
        return postRepository.getTotal();
    }

    @Override
    public void checkIsExist(UUID postUuid) {
        if (!postRepository.isExist(postUuid)) {
            throw new PostNotFoundException("Пост с uuid=" + postUuid + " не найден");
        }
    }

    @Override
    @Transactional
    public Post save(Post post) {
        UUID postUuid = postRepository.save(PostDaoMapper.postToPostDao(post));
        updatePostTags(postUuid, post.getTags(), false);

        Post savedPost = PostDaoMapper.postDaoToPost(postRepository.get(postUuid));
        return enrichPostWithTagsAndComments(savedPost);
    }

    @Override
    @Transactional
    public void delete(UUID uuid) {
        postRepository.deleteBy(uuid);
    }

    /**
     * Установить теги поста
     *
     * @param postUuid  UUID поста
     * @param tags      Коллекция тегов
     * @param isUpdated Флаг, указывающий, нужно ли удалять старые теги
     */
    private void updatePostTags(UUID postUuid, List<Tag> tags, boolean isUpdated) {
        if (tags != null) {
            List<UUID> postTagUuids = tags.stream().map(Tag::getUuid).toList();
            if (isUpdated) {
                tagService.deleteAllBy(postUuid);
            }
            tagService.batchUpdatePostTags(postUuid, postTagUuids);
        }
    }

    /**
     * Установить теги и комментарии поста
     *
     * @param post Пост
     * @return Обогащенный пост
     */
    private <P extends BasePost> P enrichPostWithTagsAndComments(P post) {
        UUID postUuid = post.getUuid();
        post.setTags(tagService.getAllBy(postUuid));
        post.setComments(commentService.getAllBy(postUuid));
        return post;
    }

    /**
     * Установить теги и комментарии постам
     *
     * @param posts Коллекция постов
     * @return Обогащенная коллекция постов
     */
    private <P extends BasePost> List<P> enrichPostsWithTagsAndComments(List<P> posts) {
        posts.forEach(this::enrichPostWithTagsAndComments);
        return posts;
    }
}