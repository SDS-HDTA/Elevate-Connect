package org.sds.elevateconnect.service.interfaces;

import org.sds.elevateconnect.dto.PostDetail;
import org.sds.elevateconnect.model.Post;

import java.util.List;

public interface IPostService {
    int addPost(Post post);
    List<PostDetail> getPostsByProjectId(Integer channelId);
}
