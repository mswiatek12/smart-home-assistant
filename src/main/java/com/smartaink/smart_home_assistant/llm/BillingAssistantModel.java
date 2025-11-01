package com.smartaink.smart_home_assistant.llm;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;

public interface BillingAssistantModel { String chat(@MemoryId String memoryId, @UserMessage String prompt); }
