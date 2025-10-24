# Hello! :bowtie:
### Dear recruiter,
This repository contains my implementation for a smart home assistant coding task. The goal was to create a conversational AI that helps users manage and maintain smart home devices, specifically a Smart Doorlock and a Smart Thermostat.

The assistant consists of two collaborating agents:

- **Agent A (Technical Specialist):** Answers technical questions using documentation for each device, including user manuals, troubleshooting notes, and integration guides. The agent only provides answers backed by these documents, asking for clarification if the information is not available.

- **Agent B (Billing Specialist):** Handles billing-related queries, with capabilities such as explaining access to invoices, receipts, and statements, outlining the refund policy, sending refund templates, and providing tips for submitting refund requests.

When a user sends a message, the system automatically routes it to the most appropriate agent, ensuring the response comes from the agent best suited for the query.

More detailed technical information and architectural decisions can be found in the **System Overview** section below.

### :warning: Disclaimer

For this project, I chose **not to use a database**. The focus was on implementing the **core functionality and proper mechanisms**.
Persistent storage of vectors or session IDs was **not considered essential for fulfilling the assignment requirements**, but it would be a natural next step for extending the application and improving scalability.



# System Overview

Once the application starts, the following steps happen in order:

### 1. Document Loading
- [DocumentLoader](src/main/java/com/smartaink/smart_home_assistant/utils/DocumentLoader.java) reads documents from directories specified in the application properties.
- Each file is tokenized and split into **text segments**, which are later used for semantic search.

### 2. Embedding Creation
- [EmbeddingService](src/main/java/com/smartaink/smart_home_assistant/service/EmbeddingService.java) generates embeddings for each text segment.
- This process runs in a background thread using the OpenAI embedding model (`text-embedding-3-large`).
- Separate **vector stores** are created for Technical Agent (Agent A) and Billing Agent (Agent B), storing the embeddings for semantic search.

### 3. Prompt Handling / Agent Routing
- The application is ready to accept user prompts through the UI.
- Each prompt triggers a **POST request** to the controller ([ChatController](src/main/java/com.smartaink.smart_home_assistant/controller/ChatController.java)), which passes it to [ChatService](src/main/java/com.smartaink.smart_home_assistant/service/ChatService.java).
- [RouterAgent](src/main/java/com/smartaink/smart_home_assistant/service/RouterAgent.java) evaluates which agent should handle the query by comparing the prompt embedding with each agent’s vector store using **cosine similarity**.
- If no agent is considered suitable, the system requests clarification from the user.

### 4. Agent Response Generation
- Once the appropriate agent is chosen, it retrieves the most relevant **document segments** from its vector store.
- These segments are converted back into text (retrieval after semantic search) and combined with the user’s prompt as context for the LLM by an agent.
- The agent calls the **LLM** (OpenAI `gpt-4o-mini`) with this context to generate a response.


### 5. Multi-Turn Memory Handling
- [LangChainConfig](src/main/java/com/smartaink/smart_home_assistant/config/LangChainConfig.java) defines a `ChatMemoryProvider` bean that creates a `MessageWindowChatMemory` for each session (`memoryId`) with a maximum of 10 messages stored.
- [ChatModel](src/main/java/com/smartaink/smart_home_assistant/llm/ChatModel.java) interface handles LLM calls with `memoryId` and the user prompt (`@UserMessage`), ensuring conversation history is preserved across multiple turns.
- In the frontend ([app.js](src/main/resources/static/js/app.js)), a unique `sessionId` is generated **once per chat session** (e.g., via `crypto.randomUUID()`) when the user opens the chat.
- It is sent as `memoryId` with each request, ensuring that the conversation history is preserved and tied to the same user session.

### 6. Response Delivery
- The generated answer is returned via the API to the UI and displayed in the chat window.
- Multi-turn conversations are supported, with session-specific memory maintained for context-aware responses.

---

This architecture ensures that the assistant provides accurate, context-aware answers, dynamically selecting the most suitable agent for each user query while maintaining conversation history per session.

# Diagram
![img_1.png](assets/flowDiagram.png)

# Screenshot from App
![img.png](assets/appScreenshot.png)

# Tech Stack

- `Backend Framework:` Spring Boot 3.5.6
- `Programming Language:` Java 21 :coffee:
- `Build Tool:` Gradle
- `LLM Integration:` LangChain4J 1.7.1
- `AI Provider:` OpenAI API (gpt-4o-mini for chat, text-embedding-3-large for embeddings)
- `Frontend:` HTML, CSS, JavaScript (for the chat UI)

# Setup Instructions

Follow these steps to set up and run the Smart Home Assistant locally.

---

## Prerequisites

You will need these installed:

- **Java 21** – [Download Java 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
- **Gradle** – [Install Gradle](https://gradle.org/install/)
- **OpenAI API key** – [Get an API key](https://platform.openai.com/account/api-keys) for LLM functionality


Set the API key as an environment variable in application.properties:

```
spring.application.name=smart-home-assistant

langchain4j.open-ai.chat-model.api-key= <PASTE IT HERE>
langchain4j.open-ai.chat-model.model-name=gpt-4o-mini
langchain4j.open-ai.embedding-model.model-name=text-embedding-3-large
langchain4j.open-ai.chat-model.temperature=0
```
This file should be in [resources](src/main/resources).

## Clone the repo
```
git clone https://github.com/mswiatek12/smart-home-assistant.git
cd smart-home-assistant
```

## Build & Run the Application
```
.\gradlew build && .\gradlew bootRun
```
The application will run by default at http://localhost:8080

Once on the site you can use the input field to communicate with the assistant or do it via curl sending a POST type request to http://localhost:8080/api/chat like:
```
curl -X POST http://localhost:8080/api/chat \
     -H "Content-Type: application/json" \
     -d '{"sessionId": "some number", "prompt": "your prompt"}'
```

# Limitations

- :warning: Currently, the assistant only supports **Smart Doorlock** and **Smart Thermostat** devices. Other devices are not integrated yet.
- :warning: No list with historical chats with agent(like e.g. in ChatGPT). If we open new session new sessionID is generated losing previous.
- :warning: Multi-turn conversation memory is **stored in-memory(not-DB)** and resets after every application restart. This means chat history is lost when the server is stopped.