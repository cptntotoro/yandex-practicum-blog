package ru.practicum.post.service;

import ru.practicum.post.Post;

import java.util.UUID;

public interface PostService {
    Object getAll();

    Object getById(UUID uuid);

    void add(Post post);

    void delete(UUID uuid);
}
