package ru.practicum.mapper.post;

import ru.practicum.dao.post.PostDao;
import ru.practicum.model.post.Post;
import ru.practicum.model.post.PostPreview;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Маппер постов для БД
 */
public class PostDaoMapper {
    /**
     * Смаппить пост в пост для БД
     *
     * @param post Пост
     * @return Пост для БД
     */
    public static PostDao postToPostDao(Post post) {
        return new PostDao.Builder()
                .uuid(post.getUuid())
                .title(post.getTitle())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .dateTime(post.getDateTime())
                .likesCounter(post.getLikesCounter())
                .build();
    }

    /**
     * Смаппить пост для БД в пост
     *
     * @param postDao Пост для БД
     * @return Пост
     */
    public static Post postDaoToPost(PostDao postDao) {
        return new Post.Builder()
                .uuid(postDao.getUuid())
                .title(postDao.getTitle())
                .content(postDao.getContent())
                .imageUrl(postDao.getImageUrl())
                .dateTime(postDao.getDateTime())
                .likesCounter(postDao.getLikesCounter())
                .build();
    }

    /**
     * Смаппить список постов для БД в список превью постов
     * @param postDaoList Список постов для БД
     * @return Список превью постов
     */
    public static List<PostPreview> postDaoListToPostPreviewList(List<PostDao> postDaoList) {
        return postDaoList.stream()
                .map(PostDaoMapper::postDaoToPostPreview)
                .collect(Collectors.toList());
    }

    /**
     * Смаппить пост для БД в превью поста
     * @param postDao Пост для БД
     * @return Превью поста
     */
    private static PostPreview postDaoToPostPreview(PostDao postDao) {
        return new PostPreview.Builder()
                .uuid(postDao.getUuid())
                .title(postDao.getTitle())
                .preview(postDao.getPreview())
                .imageUrl(postDao.getImageUrl())
                .dateTime(postDao.getDateTime())
                .likesCounter(postDao.getLikesCounter())
                .build();
    }
}
