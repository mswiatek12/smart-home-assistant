package com.smartaink.smart_home_assistant.controller;

import com.smartaink.smart_home_assistant.model.ChatRequest;
import com.smartaink.smart_home_assistant.service.ChatService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chat")
    public String openChat(){
        return "redirect:/index.html";
    }

    @PostMapping("/chat")
    @ResponseBody
    public String chat(@RequestBody ChatRequest request) {
        return chatService.chat(request.getSessionId(), request.getPrompt());
    }
}
