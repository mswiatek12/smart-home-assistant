package com.smartaink.smart_home_assistant.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouterAgent {

    private List<ConversationalAgent> agents;

    public RouterAgent(List<ConversationalAgent> agents) {
        this.agents = agents;
    }

    public String route(String sessionId, String userPrompt) {
        for (ConversationalAgent agent : this.agents) {
            if (agent.canHelp(userPrompt)) {
                return agent.answer(sessionId, userPrompt);
            }
        }
        return "Please specify.";
    }
}
