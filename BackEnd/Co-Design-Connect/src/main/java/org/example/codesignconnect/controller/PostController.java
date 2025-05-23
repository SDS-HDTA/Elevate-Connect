package org.example.codesignconnect.controller;

import org.example.codesignconnect.dto.PostDetail;
import org.example.codesignconnect.model.Post;
import org.example.codesignconnect.model.Result;
import org.example.codesignconnect.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/projects/{projectId}/channel/post")
    public Result createPost(Post post) {
        int rows = postService.addPost(post);
        return rows > 0 ? Result.success() : Result.error("Failed to create post");
    }

    @GetMapping("/projects/{channelId}/posts")
    public Result getPostsByChannelId(@PathVariable Integer channelId) {
        List<PostDetail> posts = postService.getPostsByChannelId(channelId);
        return Result.success(posts);
    }
}
