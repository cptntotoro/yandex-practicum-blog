package ru.practicum.post;

import ru.practicum.comment.Comment;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private Long id;
    private String title;
    private String imageUrl;
    private String content;
    private String tags;
    private int likes;
    private List<Comment> comments = new ArrayList<>();

}
