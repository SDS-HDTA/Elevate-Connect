package org.example.codesignconnect.service;

import org.example.codesignconnect.dto.PostDetail;
import org.example.codesignconnect.model.Post;

import java.util.List;

public interface PostService {
<<<<<<< HEAD
    int createPost(Post post);
    List<Post> getPostsByChannelId(Integer channelId);
    boolean deletePost(Integer postId);
=======
    int addPost(Post post);
    List<PostDetail> getPostsByChannelId(Integer channelId);
>>>>>>> origin/dev
}
