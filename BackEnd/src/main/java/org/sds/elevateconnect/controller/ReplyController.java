package org.sds.elevateconnect.controller;

import org.sds.elevateconnect.config.security.RequirePermission;
import org.sds.elevateconnect.dto.ReplyDetail;
import org.sds.elevateconnect.model.auth.Permission;
import org.sds.elevateconnect.model.project.Reply;
import org.sds.elevateconnect.model.Result;
import org.sds.elevateconnect.service.interfaces.IReplyService;
import org.sds.elevateconnect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReplyController {
    @Autowired
    private IReplyService replyService;
    @Autowired
    private UserService userService;

    @RequirePermission(Permission.CREATE_NEW_REPLY)
    @PostMapping("/projects/{projectId}/reply")
    public Result createReply(@RequestBody Reply reply) {
        int rows = replyService.addReply(reply);
        String authorName = userService.getFullNameById(reply.getAuthorId());

        return rows > 0 ? Result.success(new ReplyDetail(reply, authorName)) : Result.error("Failed to create reply");
    }

    @RequirePermission(Permission.ACCESS_POSTS_PAGE)
    @GetMapping("/post/{postId}")
    public Result getRepliesByPostId(@PathVariable Integer postId) {
        List<Reply> replies = replyService.getRepliesByPostId(postId);
        return Result.success(replies);
    }
}
