package org.example.codesignconnect.model;

import lombok.Data;

@Data
public class ResultMessage {
    private boolean isSystem;
    private String fromName;
    private Object message;
}
