package org.example.codesignconnect.service.interfaces;

import org.example.codesignconnect.dto.PostDetail;
import org.example.codesignconnect.model.Post;

import java.util.List;

public interface IPostService {
    int addPost(Post post);
    List<PostDetail> getPostsByProjectId(Integer channelId);
}
