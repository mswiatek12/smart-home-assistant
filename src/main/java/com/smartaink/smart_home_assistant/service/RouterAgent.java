package com.smartaink.smart_home_assistant.service;

import java.io.StringReader;

import com.smartaink.smart_home_assistant.llm.RouterAssistantModel;
import dev.langchain4j.service.Result;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouterAgent {

    private RouterAssistantModel routerAssistantModel;

    private List<ConversationalAgent> agents;

    private final String routeContext = """
            You are an AI router.
            Given a user question, decide which agent should answer (TechnicalAgent/BillingAgent).
            Only respond in JSON format like:
            
            {
              "agent": "<agent name>"
            }
            
            User question: 
            """;


    public RouterAgent(RouterAssistantModel routerAssistantModel, List<ConversationalAgent> agents) {
        this.routerAssistantModel = routerAssistantModel;
        this.agents = agents;
    }

    public String route(String sessionId, String userPrompt) {
        if(agents.isEmpty()){
            return "No agent is available";
        }

        Result<String> result = routerAssistantModel.chat(sessionId, routeContext + userPrompt);
        try {
            String jsonResponse = result.content();
            JsonReader reader = Json.createReader(new StringReader(jsonResponse));
            JsonObject obj = reader.readObject();
            String agent = obj.getString("agent");

            if (agent != null && !(agent.isBlank())) {
                for (ConversationalAgent a : agents) {
                    if (a.getName().equals(agent)) {
                        return a.answer(sessionId, userPrompt);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("ERR couldn't parse JSON :-( ");
        }

        String fallbackPrompt = "Context: You are an agent router. " +
                "The given information was not enough. Ask for a more detailed message or call a tool. " +
                "userPrompt: " + userPrompt;

        return routerAssistantModel.chat(sessionId, fallbackPrompt).content();
    }
}
