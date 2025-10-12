package org.sds.elevateconnect.service;

import org.sds.elevateconnect.dto.PostDetail;
import org.sds.elevateconnect.dto.ReplyDetail;
import org.sds.elevateconnect.mapper.PostMapper;
import org.sds.elevateconnect.mapper.ReplyMapper;
import org.sds.elevateconnect.mapper.UserMapper;
import org.sds.elevateconnect.model.project.Post;
import org.sds.elevateconnect.model.project.Reply;
import org.sds.elevateconnect.service.interfaces.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService implements IPostService {

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
    public List<PostDetail> getPostsByProjectId(Integer projectId) {
        List<Post> posts = postMapper.getPostsByProjectId(projectId);
        List<PostDetail> postDetails = new ArrayList<>();

        //Translate Post objects into PostDetail objects that contain replies
        for (Post post : posts) {
            //Fetch all replies for current post
            List<Reply> replies = replyMapper.getRepliesByPostId(post.getId());

            //Fetch author's name
            String authorName = userMapper.getFullNameById(post.getAuthorId());
            List<ReplyDetail> replyDetails = new ArrayList<>();
            for (Reply reply : replies) {
                replyDetails.add(new ReplyDetail(reply, userMapper.getFullNameById(reply.getAuthorId())));
            }
            postDetails.add(new PostDetail(post, replyDetails, authorName));
        }
        return postDetails;
    }
}
