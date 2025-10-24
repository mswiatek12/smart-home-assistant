package com.smartaink.smart_home_assistant.llm;

import com.smartaink.smart_home_assistant.utils.DocumentLoader;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.exception.HttpException;
import dev.langchain4j.exception.InvalidRequestException;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
public class EmbeddingService {

    private EmbeddingModel embeddingModel;

    private final DocumentLoader documentLoader;

    private final List<EmbeddingStore<TextSegment>> technicalEmbeddingStores;
    private final List<EmbeddingStore<TextSegment>> billingEmbeddingStores;

    @Value("${langchain4j.open-ai.chat-model.api-key}")
    private String apiKey;

    @Value("${langchain4j.open-ai.embedding-model.model-name}")
    private String model;


    public EmbeddingService(DocumentLoader documentLoader) {
        this.documentLoader = documentLoader;
        this.technicalEmbeddingStores = new ArrayList<>();
        this.billingEmbeddingStores = new ArrayList<>();
    }


    @PostConstruct
    public void init() {
        new Thread(() -> initEmbeddings()).start();
    }

    private void initEmbeddings() {
        try {
            this.embeddingModel = OpenAiEmbeddingModel.builder()
                    .apiKey(apiKey)
                    .modelName(model)
                    .build();
            List<List<TextSegment>> tsegments = documentLoader.loadTechnicalDocuments();
            for (List<TextSegment> segment : tsegments) {
                List<Embedding> embeddings = embeddingModel.embedAll(segment).content();
                EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
                embeddingStore.addAll(embeddings, segment);

                technicalEmbeddingStores.add(embeddingStore);
            }

            ///TO DO: DELETE CODE DUPLICATION BELOW

            List<List<TextSegment>> bsegments = documentLoader.loadBillingDocuments();
            for (List<TextSegment> segment : bsegments) {
                List<Embedding> embeddings = embeddingModel.embedAll(segment).content();
                EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
                embeddingStore.addAll(embeddings, segment);

                billingEmbeddingStores.add(embeddingStore);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
