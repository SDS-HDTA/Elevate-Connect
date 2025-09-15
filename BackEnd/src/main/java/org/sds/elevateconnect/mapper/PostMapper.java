package org.sds.elevateconnect.mapper;

import org.sds.elevateconnect.model.project.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
    int insertPost(Post post);
    List<Post> getPostsByProjectId(Integer projectId);
}
