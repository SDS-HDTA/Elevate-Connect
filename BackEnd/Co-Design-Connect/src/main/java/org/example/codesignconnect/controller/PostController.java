package org.example.codesignconnect.controller;

import org.example.codesignconnect.model.Post;
import org.example.codesignconnect.model.Result;
import org.example.codesignconnect.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public Result createPost(@RequestBody Post post) {
        int rows = postService.addPost(post);
        return rows > 0 ? Result.success() : Result.error("Failed to create post");
    }

    @GetMapping("/channel/{channelId}")
    public Result getPostsByChannelId(@PathVariable Integer channelId) {
        List<Post> posts = postService.getPostsByChannelId(channelId);
        return Result.success(posts);
    }
}
