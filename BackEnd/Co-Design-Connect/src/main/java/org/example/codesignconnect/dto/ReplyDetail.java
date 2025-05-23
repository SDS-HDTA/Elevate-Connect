package org.example.codesignconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.codesignconnect.model.Reply;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDetail {
    private Reply reply;
    private String senderName;
}
