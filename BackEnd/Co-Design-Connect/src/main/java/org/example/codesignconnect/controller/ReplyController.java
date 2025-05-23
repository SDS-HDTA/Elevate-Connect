package org.example.codesignconnect.controller;

import org.example.codesignconnect.config.ProjectChannelEndpoint;
import org.example.codesignconnect.dto.ReplyDetail;
import org.example.codesignconnect.model.Reply;
import org.example.codesignconnect.model.Result;
import org.example.codesignconnect.model.User;
import org.example.codesignconnect.service.ReplyService;
import org.example.codesignconnect.service.UserService;
import org.example.codesignconnect.utils.WebSocketMessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @Autowired
    private UserService userService;

    @PostMapping("/projects/{projectId}/channel/reply")
    public Result createReply(Reply reply) {
        int rows = replyService.createReply(reply);
        String username = ((User) userService.getUserInfo(reply.getAuthorId()).getData()).getUsername();
        return rows > 0 ? Result.success(new ReplyDetail(reply, username)) : Result.error("Failed to create reply");
    }
    public Result replyToPost(@PathVariable("projectId") Integer projectId,
                              @RequestParam("postId") Integer postId,
                              @RequestParam("channelId") Integer channelId,
                              @RequestParam("content") String content,
                              @RequestParam("createTime") String createTime,
                              @RequestParam("userId") Integer userId) {

        Reply reply = new Reply();
        reply.setPostId(postId);
        reply.setChannelId(channelId);
        reply.setAuthorId(userId);
        reply.setContent(content);
        reply.setCreateTime(Timestamp.valueOf(createTime));

        int result = replyService.createReply(reply);
        if (result > 0) {
            ProjectChannelEndpoint.sendToProject(
                    projectId,
                    WebSocketMessageFactory.createNewReplyMessage(reply.getId())
            );
            return Result.success(reply);
        } else {
            return Result.error("Failed to reply to post");
        }
    }

    @GetMapping("/post/{postId}")
    public Result getRepliesByPostId(@PathVariable Integer postId) {
        List<Reply> replies = replyService.getRepliesByPostId(postId);
        return Result.success(replies);
    }

    @DeleteMapping("/projects/{projectId}/channel/post/{postId}/reply/{replyId}")
    public Result deleteReply(@PathVariable("projectId") Integer projectId,
                              @PathVariable("postId") Integer postId,
                              @PathVariable("replyId") Integer replyId) {
        boolean success = replyService.deleteReply(replyId);
        if (success) {
            ProjectChannelEndpoint.sendToProject(projectId, WebSocketMessageFactory.createDeleteReplyMessage(replyId));

            return Result.success("Reply deleted successfully");
        } else {
            return Result.error("Failed to delete reply");
        }
    }

}
