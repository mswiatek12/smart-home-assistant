package com.smartaink.smart_home_assistant.service;

import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final RouterAgent routerAgent;

    public ChatService(RouterAgent routerAgent) {
        this.routerAgent = routerAgent;
    }

    public String chat(String sessionId, String userPrompt) {
        return routerAgent.route(sessionId, userPrompt);
    }
}
