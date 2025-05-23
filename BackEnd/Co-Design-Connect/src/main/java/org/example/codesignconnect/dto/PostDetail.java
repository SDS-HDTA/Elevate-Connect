package org.example.codesignconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.codesignconnect.model.Post;
import org.example.codesignconnect.model.Reply;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDetail {
    private Post post;
    private List<Reply> replies;
    private String creatorName;
}
