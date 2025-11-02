package com.smartaink.smart_home_assistant.llm;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

// to avoid using tools for router we explicitly set configuration
@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "openAiChatModel",
        chatMemoryProvider = "chatMemoryProvider"
)
public interface RouterAssistantModel {
    Result<String> chat(@MemoryId String memoryId, @UserMessage String prompt);
}