package org.example.codesignconnect.service;

import org.example.codesignconnect.model.Post;

import java.util.List;

public interface PostService {
    int createPost(Post post);
    List<Post> getPostsByChannelId(Integer channelId);
    boolean deletePost(Integer postId);
}
