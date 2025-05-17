package org.example.codesignconnect.service.impl;

import org.example.codesignconnect.mapper.PostMapper;
import org.example.codesignconnect.model.Post;
import org.example.codesignconnect.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Override
    public int addPost(Post post) {
        return postMapper.insertPost(post);
    }

    @Override
    public List<Post> getPostsByChannelId(Integer channelId) {
        return postMapper.getPostsByChannelId(channelId);
    }
}
