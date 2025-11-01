package com.smartaink.smart_home_assistant.service;




public interface ConversationalAgent {
    String getName();
    boolean canHelp(String userPrompt);
    String answer(String SessionId, String userPrompt);
}
