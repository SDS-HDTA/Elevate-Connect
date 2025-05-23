package org.example.codesignconnect.controller;

import org.example.codesignconnect.model.Reply;
import org.example.codesignconnect.model.Result;
import org.example.codesignconnect.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @PostMapping("/projects/{projectId}/channel/reply")
    public Result createReply(Reply reply) {
        int rows = replyService.addReply(reply);
        return rows > 0 ? Result.success() : Result.error("Failed to create reply");
    }

    @GetMapping("/post/{postId}")
    public Result getRepliesByPostId(@PathVariable Integer postId) {
        List<Reply> replies = replyService.getRepliesByPostId(postId);
        return Result.success(replies);
    }
}
