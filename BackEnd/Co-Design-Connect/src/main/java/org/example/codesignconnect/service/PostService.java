package org.example.codesignconnect.service;

import org.example.codesignconnect.model.Post;

import java.util.List;

public interface PostService {
    int addPost(Post post);
    List<Post> getPostsByChannelId(Integer channelId);
}
