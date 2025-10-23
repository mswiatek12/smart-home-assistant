package com.smartaink.smart_home_assistant.llm;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface ChatModel { Result<String> chat(@MemoryId String memoryId, @UserMessage String prompt); }
