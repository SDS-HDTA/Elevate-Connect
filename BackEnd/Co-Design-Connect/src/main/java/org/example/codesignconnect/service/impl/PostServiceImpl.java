package org.example.codesignconnect.service.impl;

import org.example.codesignconnect.dto.PostDetail;
import org.example.codesignconnect.mapper.PostMapper;
import org.example.codesignconnect.mapper.ReplyMapper;
import org.example.codesignconnect.mapper.UserMapper;
import org.example.codesignconnect.model.Post;
import org.example.codesignconnect.model.Reply;
import org.example.codesignconnect.model.User;
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
    public int createPost(Post post) {
        return postMapper.insertPost(post);
    }

    @Override
    public List<PostDetail> getPostsByChannelId(Integer channelId) {
        List<Post> posts = postMapper.getPostsByChannelId(channelId);
        List<PostDetail> postDetails = new ArrayList<>();
        for(Post post : posts){
            List<Reply> replies = replyMapper.getRepliesByPostId(post.getId());
            User user = userMapper.getUserById(post.getAuthorId());
            postDetails.add(new PostDetail(post, replies, user.getUsername()));
        }
        return postDetails;
    }

    @Override
    public boolean deletePost(Integer postId) {
        return postMapper.deletePost(postId) > 0;
    }
}
