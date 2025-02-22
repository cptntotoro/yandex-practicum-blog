package ru.practicum.post.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.post.Post;
import ru.practicum.post.service.PostService;

import java.util.UUID;

@Controller
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public String getAllPosts(Model model) {
        model.addAttribute("posts", postService.getAll());
        return "posts";
    }

    @GetMapping("/{uuid}")
    public String getPostById(@PathVariable UUID uuid, Model model) {
        model.addAttribute("post", postService.getById(uuid));
        return "post";
    }

    @PostMapping
    public String addPost(@ModelAttribute Post post) {
        postService.add(post);
        return "redirect:/posts";
    }

    @DeleteMapping("/{uuid}")
    public String deletePost(@PathVariable UUID uuid) {
        postService.delete(uuid);
        return "redirect:/posts";
    }
}
