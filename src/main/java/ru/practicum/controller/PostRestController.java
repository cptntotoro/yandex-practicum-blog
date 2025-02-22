package ru.practicum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentUpdateDto;
import ru.practicum.mapper.comment.CommentMapper;
import ru.practicum.service.comment.CommentService;
import ru.practicum.service.post.PostService;

import java.util.UUID;

@RestController
@RequestMapping("/posts")
public class PostRestController {

    /**
     * Сервис управления постами
     */
    private PostService postService;
    /**
     * Сервис управления комментариями
     */
    private CommentService commentService;

    @Autowired
    public void setPostService(PostService postService) {
        this.postService = postService;
    }

    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    @PutMapping("/{postUuid}/like")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void setLike(@PathVariable("postUuid") UUID postUuid) {
        postService.setLike(postUuid);
    }

    @PostMapping("/{postUuid}/comments/{commentUuid}")
    public void updateComment(@PathVariable("postUuid") UUID postUuid,
                              @PathVariable("commentUuid") UUID commentUuid,
                              @RequestBody CommentUpdateDto commentUpdateDto) {
        commentService.update(CommentMapper.commentUpdateDtoToComment(postUuid, commentUuid, commentUpdateDto));
    }

    @DeleteMapping("/{postUuid}/comments/{commentUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable("postUuid") UUID postUuid, @PathVariable("commentUuid") UUID commentUuid) {
        commentService.delete(commentUuid);
    }

    @DeleteMapping("/{postUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("postUuid") UUID postUuid) {
        postService.delete(postUuid);
    }
}
