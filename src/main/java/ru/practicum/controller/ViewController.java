package ru.practicum.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ru.practicum.dto.comment.CommentAddDto;
import ru.practicum.dto.comment.CommentUpdateDto;
import ru.practicum.dto.post.*;
import ru.practicum.mapper.comment.CommentMapper;
import ru.practicum.mapper.tag.TagMapper;
import ru.practicum.model.post.Post;
import ru.practicum.mapper.post.PostMapper;
import ru.practicum.model.post.PostPreview;
import ru.practicum.service.comment.CommentService;
import ru.practicum.service.post.PostService;
import ru.practicum.service.tag.TagService;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/posts")
public class ViewController {
    /**
     * Сервис управления постами
     */
    private PostService postService;
    /**
     * Сервис управления комментариями
     */
    private CommentService commentService;
    /**
     * Сервис управления тегами
     */
    private TagService tagService;

    @Autowired
    public void setPostService(PostService postService) {
        this.postService = postService;
    }

    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    @Autowired
    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public String getAllByPage(@RequestParam(name = "from", defaultValue = "1") Integer page,
                               @RequestParam(name = "size", defaultValue = "10") Integer size,
                               Model model) {
        List<PostPreview> postPage = postService.getPage(page, size);
        model.addAttribute("posts", PostMapper.postsToPostNewsfeedDtoList(postPage).stream().toList());

        long totalPosts = postService.getTotal();
        int totalPages = (int) Math.ceil((double) totalPosts / size);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);
        model.addAttribute("from", page);
        model.addAttribute("modifyPost", new PostAddDto());

        model.addAttribute("tags", TagMapper.tagsToTagViewDtoList(tagService.getAll()));
        return "newsfeed";
    }

    @GetMapping("/{postUuid}")
    public String getByUuid(@PathVariable("postUuid") UUID postUuid, ModelMap model) {
        Post post = postService.get(postUuid);
        PostViewDto postViewDto = PostMapper.postToPostViewDto(post);
        model.addAttribute("post", postViewDto);
        model.addAttribute("tags", TagMapper.tagsToTagViewDtoList(tagService.getAll()));
        model.addAttribute("modifyPost", new PostUpdateDto());
        model.addAttribute("newComment", new CommentAddDto());
        model.addAttribute("updatedComment", new CommentUpdateDto());
        return "post";
    }

    @PostMapping
    public RedirectView add(@ModelAttribute("modifyPost") PostAddDto newPost,
                            HttpServletRequest request,
                            RedirectAttributes redirectAttributes) throws MalformedURLException {

        PostAddDto postAddDto = PostAddDto.newBuilder()
                .title(request.getParameter("title"))
                .content(request.getParameter("content"))
                .imageUrl(URI.create(request.getParameter("imageUrl")).toURL())
                .tags(Arrays.stream(request.getParameterValues("tags"))
                        .map(UUID::fromString)
                        .collect(Collectors.toList()))
                .build();

        Post post = postService.save(PostMapper.postAddDtoToPost(postAddDto));

        redirectAttributes.addFlashAttribute("post", PostMapper.postToPostViewDto(post));

        return getRedirectView("/posts/" + post.getUuid());
    }

    @PostMapping("/{postUuid}")
    public RedirectView update(@PathVariable("postUuid") UUID postUuid,
                               @ModelAttribute("modifyPost") PostUpdateDto postUpdateDto,
                               HttpServletRequest request,
                               RedirectAttributes redirectAttributes) throws MalformedURLException {

        PostUpdateDto postUpdate = PostUpdateDto.newBuilder()
                .title(request.getParameter("title"))
                .content(request.getParameter("content"))
                .imageUrl(URI.create(request.getParameter("imageUrl")).toURL())
                .tags(Arrays.stream(request.getParameterValues("tags"))
                        .map(UUID::fromString)
                        .collect(Collectors.toList()))
                .build();

        Post post = postService.update(PostMapper.postUpdateDtoToPost(postUuid, postUpdate));

        redirectAttributes.addFlashAttribute("post", PostMapper.postToPostViewDto(post));

        return getRedirectView("/posts/" + postUuid);
    }

    @PostMapping("/{postUuid}/comments")
    public RedirectView addComment(@PathVariable("postUuid") UUID postUuid,
                                   @ModelAttribute("newComment") CommentAddDto commentAddDto,
                                   RedirectAttributes redirectAttributes) {
        commentService.save(CommentMapper.commentAddDtoToComment(postUuid, commentAddDto));

        Post post = postService.get(postUuid);
        PostViewDto postViewDto = PostMapper.postToPostViewDto(post);
        redirectAttributes.addFlashAttribute("post", postViewDto);

        return getRedirectView("/posts/" + postUuid);
    }

    @GetMapping("/tag/{tagUuid}")
    public String getAllByTag(@PathVariable("tagUuid") UUID tagUuid, Model model) {
        List<PostPreview> postList = postService.getAllByTag(tagUuid);
        List<PostNewsfeedDto> postNewsfeedDtoList = PostMapper.postsToPostNewsfeedDtoList(postList);
        model.addAttribute("posts", postNewsfeedDtoList);
        model.addAttribute("tags", TagMapper.tagsToTagViewDtoList(tagService.getAll()));
        model.addAttribute("modifyPost", new PostAddDto());
        return "newsfeed";
    }

    private RedirectView getRedirectView(String url) {
        RedirectView redirectView = new RedirectView(url, true);
        redirectView.setHttp10Compatible(false);
        return redirectView;
    }
}
