package com.smartaink.smart_home_assistant.controller;

import com.smartaink.smart_home_assistant.model.ChatRequest;
import com.smartaink.smart_home_assistant.service.ChatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public String chat(@RequestBody ChatRequest request) {
        return chatService.chat(request.getSessionId(), request.getPrompt());
    }
}
