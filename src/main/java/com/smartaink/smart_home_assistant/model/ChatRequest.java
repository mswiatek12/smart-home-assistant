package com.smartaink.smart_home_assistant.model;

import lombok.Getter;

@Getter
public class ChatRequest {
    private String sessionId;
    private String prompt;
}
