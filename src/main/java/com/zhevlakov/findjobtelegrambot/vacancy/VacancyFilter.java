package com.zhevlakov.findjobtelegrambot.vacancy;

import com.zhevlakov.findjobtelegrambot.user.query.UserQueryService;
import com.zhevlakov.findjobtelegrambot.vacancy.filter.EmbeddingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VacancyFilter {
    private final EmbeddingService embeddingService;
    private final UserQueryService userQueryService;
    private final Logger log = LoggerFactory.getLogger(VacancyFilter.class);


    private final static int DIMENSION = 384;

    public VacancyFilter(
            EmbeddingService embeddingService,
            UserQueryService userQueryService
    ) {
        this.embeddingService = embeddingService;
        this.userQueryService = userQueryService;
    }

    public List<VacancyDto> filterVacancy(List<VacancyDto> vacancyDtoList, List<String> additionalInfo, Long userId) {
        int vacancyAmount = vacancyDtoList.size();

        List<String> titles = vacancyDtoList.stream()
                .map(VacancyDto::title)
                .toList();

        List<float[]> titlesEmbeddings = embeddingService.embed(titles);

        String userQueryPosition = userQueryService.getByChatId(userId).getPosition();

        float[] positionEmbedding = embeddingService.embed(userQueryPosition);

        float[] score = getScore(positionEmbedding, titlesEmbeddings);
        log.info("Итоговый score: {}", score);


        List<VacancyDto> filteredList = new ArrayList<>();
        for (int i = 0; i < vacancyAmount; i++) {
            if (score[i] > 0.6f) {
                filteredList.add(vacancyDtoList.get(i));
            }
        }

        log.info("Отфильтрованный список c релевантными вакансиями: {}", filteredList);

        return filteredList;
    }

    private float[] getScore(float[] positionEmbedding, List<float[]> titlesEmbeddings) {
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

    private float getVectorLength(float[] vector) {
        float sqrSum = 0.0f;
        for (float element : vector) {
            sqrSum += element * element;
        }

        return (float) Math.sqrt(sqrSum);
    }

    private float getScalar(float[] vector1, float[] vector2) {
        float scalar = 0.0f;
        for (int i = 0; i < DIMENSION; i++) {
            scalar += vector1[i] * vector2[i];
        }

        return scalar;
    }
}
