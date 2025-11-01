package com.smartaink.smart_home_assistant.config;

import com.smartaink.smart_home_assistant.llm.BillingAssistantModel;
import com.smartaink.smart_home_assistant.llm.TechnicalAssistantModel;
import com.smartaink.smart_home_assistant.tools.BillingTools;
import com.smartaink.smart_home_assistant.tools.TechnicalTools;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LangChainConfig {

    @Bean
    public TechnicalAssistantModel technicalAssistantModel(
            ChatModel chatModel,
            ChatMemoryProvider chatMemoryProvider,
            TechnicalTools TechnicalToolsImpl
    ){
        return AiServices.builder(TechnicalAssistantModel.class)
                .chatModel(chatModel)
                .tools(TechnicalToolsImpl)
                .chatMemoryProvider(chatMemoryProvider)
                .build();
    }

    @Bean
    public BillingAssistantModel billingAssistantModel(
         ChatModel chatModel,
         ChatMemoryProvider chatMemoryProvider,
         BillingTools billingToolsImpl
    ){
        return AiServices.builder(BillingAssistantModel.class)
                .chatModel(chatModel)
                .tools(billingToolsImpl)
                .chatMemoryProvider(chatMemoryProvider)
                .build();
    }

    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(10)
                .build();
    }
}
