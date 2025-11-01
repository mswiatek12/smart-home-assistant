package com.smartaink.smart_home_assistant.service;

import com.smartaink.smart_home_assistant.llm.EmbeddingService;
import com.smartaink.smart_home_assistant.llm.TechnicalAssistantModel;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TechnicalAgent implements ConversationalAgent{

    @Autowired
    private final TechnicalAssistantModel technicalAssistantModel;
    private final EmbeddingService embeddingService;

    public TechnicalAgent(TechnicalAssistantModel technicalAssistantModel, EmbeddingService embeddingService) {
        this.technicalAssistantModel = technicalAssistantModel;
        this.embeddingService = embeddingService;
    }

    @Override
    public String getName() {
        return "TechnicalAgent";
    }

    @Override
    public boolean canHelp(String userPrompt) {
        EmbeddingModel embeddingModel = embeddingService.getEmbeddingModel();
        Embedding embeddedQuery = embeddingModel.embed(userPrompt).content();

        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                .queryEmbedding(embeddedQuery)
                .maxResults(3)
                .build();

        double bestScore = 0.0;

        for(EmbeddingStore<TextSegment> vstore : embeddingService.getTechnicalEmbeddingStores()) {
            EmbeddingSearchResult<TextSegment> result = vstore.search(request);

            if(!result.matches().isEmpty()) {
                double score = result.matches().get(0).score();
                if(score > bestScore)
                {
                    bestScore = score;
                }
            }
        }

        return bestScore > 0.76;
    }

    @Override
    public String answer(String sessionId,String userPrompt) {
        Embedding embeddedQuery = embeddingService
                .getEmbeddingModel()
                .embed(userPrompt)
                .content();

        EmbeddingSearchRequest request = EmbeddingSearchRequest
                .builder()
                .queryEmbedding(embeddedQuery)
                .maxResults(3)
                .build();

        List<String> contextSegments = new ArrayList<>();
        for(EmbeddingStore<TextSegment> vstore : embeddingService.getTechnicalEmbeddingStores()) {
            EmbeddingSearchResult<TextSegment> result = vstore.search(request);
            for(EmbeddingMatch<TextSegment> match : result.matches()) {
                TextSegment segment = match.embedded();
                contextSegments.add(segment.text());
            }
        }

        String contextText = String.join("\n\n", contextSegments);
        String fullPrompt = "Context:\n" + contextText + "\n\nUser question:\n" + userPrompt;

        return technicalAssistantModel.chat(sessionId, fullPrompt);
    }
}
