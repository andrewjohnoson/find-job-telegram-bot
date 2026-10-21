package com.zhevlakov.findjobtelegrambot.vacancy.filter;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmbeddingService {
    private final EmbeddingModel embeddingModel;

    private final static int DIMENSION = 384;
    private final static float THRESHOLD = 0.6f;

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

    public float[] getScore(float[] positionEmbedding, List<float[]> titlesEmbeddings) {
        int titlesAmount = titlesEmbeddings.size();
        float[] scores = new float[titlesAmount];

        float posEmbModule = getVectorLength(positionEmbedding);

        for (int i = 0; i < titlesAmount; i++) {
            float[] title = titlesEmbeddings.get(i);
            float titleLength = getVectorLength(title);
            scores[i] = getScalar(positionEmbedding, title) / (posEmbModule * titleLength);
        }

        return scores;
    }

    public float getVectorLength(float[] vector) {
        float sqrSum = 0.0f;
        for (float element : vector) {
            sqrSum += element * element;
        }

        return (float) Math.sqrt(sqrSum);
    }

    public float getScalar(float[] vector1, float[] vector2) {
        float scalar = 0.0f;
        for (int i = 0; i < DIMENSION; i++) {
            scalar += vector1[i] * vector2[i];
        }

        return scalar;
    }

    public <T> List<T> filterSimilar(List<T> list, float[] scores) {
        List<T> filterList = new ArrayList<>();
        int limit = Math.min(list.size(), scores.length);

        for (int i = 0; i < limit; i++) {
            if (scores[i] > THRESHOLD) {
                filterList.add(list.get(i));
            }
        }

        return filterList;
    }

    public <T> List<T> filterSimilar(List<T> list, float[] scores, float threshold) {
        List<T> filterList = new ArrayList<>();
        int limit = Math.min(list.size(), scores.length);

        for (int i = 0; i < limit; i++) {
            if (scores[i] > threshold) {
                filterList.add(list.get(i));
            }
        }

        return filterList;
    }
}
