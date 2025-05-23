package org.example.codesignconnect.service.impl;

import org.example.codesignconnect.dto.PostDetail;
import org.example.codesignconnect.mapper.PostMapper;
import org.example.codesignconnect.mapper.ReplyMapper;
import org.example.codesignconnect.mapper.UserMapper;
import org.example.codesignconnect.model.Post;
import org.example.codesignconnect.model.Reply;
import org.example.codesignconnect.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private ReplyMapper replyMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public int addPost(Post post) {
        return postMapper.insertPost(post);
    }

    @Override
    public List<PostDetail> getPostsByChannelId(Integer channelId) {
        List<Post> posts = postMapper.getPostsByChannelId(channelId);
        List<PostDetail> postDetails = new ArrayList<>();
        for(Post post : posts){
            List<Reply> replies = replyMapper.getRepliesByPostId(post.getId());
            String name = userMapper.getUserById(post.getAuthorId()).getUsername();
            postDetails.add(new PostDetail(post, replies, name));
        }
        return postDetails;
    }
}
