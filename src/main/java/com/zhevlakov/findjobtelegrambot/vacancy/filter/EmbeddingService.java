package com.zhevlakov.findjobtelegrambot.vacancy.filter;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmbeddingService {
    private final EmbeddingModel embeddingModel;

    public EmbeddingService(
            EmbeddingModel embeddingModel
    ) {
        this.embeddingModel = embeddingModel;
    }

    public float[] embed(String query) {
        return embeddingModel.embed(query);
    }

    public List<float[]> embed(List<String> textList) {
        return embeddingModel.embed(textList);
    }
}
