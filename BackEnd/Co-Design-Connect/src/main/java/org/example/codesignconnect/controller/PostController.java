package org.example.codesignconnect.controller;

<<<<<<< HEAD
import org.example.codesignconnect.config.ProjectChannelEndpoint;
=======
import org.example.codesignconnect.dto.PostDetail;
>>>>>>> origin/dev
import org.example.codesignconnect.model.Post;
import org.example.codesignconnect.model.Result;
import org.example.codesignconnect.service.PostService;
import org.example.codesignconnect.utils.WebSocketMessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/projects/{projectId}/channel/post")
<<<<<<< HEAD
    public Result createPost(@PathVariable("projectId") Integer channelId,
                             @RequestParam("content") String content,
                             @RequestParam("createTime") String createTime,
                             @RequestParam("userId") Integer authorId) {

        Post post = new Post();
        post.setChannelId(channelId);
        post.setContent(content);
        post.setCreateTime(Timestamp.valueOf(createTime));
        post.setAuthorId(authorId);

        int result = postService.createPost(post);
        if (result > 0) {
            ProjectChannelEndpoint.sendToProject(
                    channelId,
                    WebSocketMessageFactory.createNewPostMessage(post.getId())
            );
            return Result.success(post);
        } else {
            return Result.error("Post creation failed");
        }
    }



    @GetMapping("/channel/{channelId}")
=======
    public Result createPost(Post post) {
        int rows = postService.addPost(post);
        return rows > 0 ? Result.success() : Result.error("Failed to create post");
    }

    @GetMapping("/projects/{channelId}/posts")
>>>>>>> origin/dev
    public Result getPostsByChannelId(@PathVariable Integer channelId) {
        List<PostDetail> posts = postService.getPostsByChannelId(channelId);
        return Result.success(posts);
    }

    @DeleteMapping("/projects/{projectId}/channel/post/{postId}")
    public Result deletePost(@PathVariable("projectId") Integer projectId,
                             @PathVariable("postId") Integer postId) {
        boolean success = postService.deletePost(postId);
        if (success) {
            ProjectChannelEndpoint.sendToProject(projectId, WebSocketMessageFactory.createDeletePostMessage(postId));
            return Result.success("Post deleted successfully");
        } else {
            return Result.error("Failed to delete post");
        }
    }

}
