package com.smartaink.smart_home_assistant.utils;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.openai.OpenAiTokenCountEstimator;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

@Component
public class DocumentLoader {

    private final String baseDocsPath="src/main/resources/";

    private final String billingDirs[] = {
            "billingDocs/"
    };

    private final String[] billingFiles = {
            "Pricing.txt",
            "Refunds.txt",
            "Invoices.txt"
    };

    private final String[] technicalDirs = {
            "doorbellDocs/",
            "thermostatDocs/"
    };

    private final String[] technicalFiles = {
            "User_Manual.txt",
            "Troubleshooting_Notes.txt",
            "Integration_Tips.txt"
    };

    public List<List<TextSegment>> loadTechnicalDocuments() {
        return loadDocuments(technicalDirs, technicalFiles);
    }

    public List<List<TextSegment>> loadBillingDocuments() {
        return loadDocuments(billingDirs, billingFiles);
    }

    public List<List<TextSegment>> loadDocuments(String[] dirs, String[] files) {
        List<List<TextSegment>> allSegments=new ArrayList<>();
        for(String dir : dirs) {
            for(String file : files) {
                String absolutePath=baseDocsPath+dir+file;
                Document doc = FileSystemDocumentLoader.loadDocument(absolutePath);
                DocumentSplitter splitter = DocumentSplitters.recursive(100, 0, new OpenAiTokenCountEstimator("gpt-4o"));
                List<TextSegment> segments = splitter.split(doc);
                allSegments.add(segments);
            }
        }
        return allSegments;
    }
}
