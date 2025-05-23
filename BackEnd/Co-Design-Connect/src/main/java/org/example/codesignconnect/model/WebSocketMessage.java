package org.example.codesignconnect.model;

import lombok.Data;

@Data
public class WebSocketMessage {
    private String type;
    private Object data;
}
