package org.sds.elevateconnect.controller;

import org.sds.elevateconnect.config.security.RequirePermission;
import org.sds.elevateconnect.dto.PostDetail;
import org.sds.elevateconnect.model.auth.Permission;
import org.sds.elevateconnect.model.project.Post;
import org.sds.elevateconnect.model.Result;
import org.sds.elevateconnect.service.PostService;
import org.sds.elevateconnect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @RequirePermission(Permission.CREATE_NEW_POST)
    @PostMapping("/projects/{projectId}/post")
    public Result createPost(Post post) {
        int rows = postService.addPost(post);
        String name = userService.getFullNameById(post.getAuthorId());
        return rows > 0 ? Result.success(new PostDetail(post, new ArrayList<>(), name)) : Result.error("Failed to create post");
    }

    @RequirePermission(Permission.ACCESS_POSTS_PAGE)
    @GetMapping("/projects/{projectId}/posts")
    public Result getPostsByChannelId(@PathVariable Integer projectId) {
        List<PostDetail> posts = postService.getPostsByProjectId(projectId);
        return Result.success(posts);
    }
}
