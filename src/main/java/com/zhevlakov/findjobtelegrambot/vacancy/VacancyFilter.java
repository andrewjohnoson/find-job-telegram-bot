package com.zhevlakov.findjobtelegrambot.vacancy;

import com.zhevlakov.findjobtelegrambot.user.query.UserQueryService;
import com.zhevlakov.findjobtelegrambot.vacancy.filter.EmbeddingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VacancyFilter {
    private final EmbeddingService embeddingService;
    private final UserQueryService userQueryService;
    private final Logger log = LoggerFactory.getLogger(VacancyFilter.class);

    public VacancyFilter(
            EmbeddingService embeddingService,
            UserQueryService userQueryService
    ) {
        this.embeddingService = embeddingService;
        this.userQueryService = userQueryService;
    }

    public List<VacancyDto> filterVacancy(List<VacancyDto> vacancyDtoList, List<String> additionalInfo, Long userId) {
        List<String> titles = vacancyDtoList.stream()
                .map(VacancyDto::title)
                .toList();

        List<float[]> titlesEmbeddings = embeddingService.embed(titles);

        String userQueryPosition = userQueryService.getByChatId(userId).getPosition();

        float[] positionEmbedding = embeddingService.embed(userQueryPosition);

        float[] score = embeddingService.getScore(positionEmbedding, titlesEmbeddings);
        log.info("Итоговый score: {}", score);

        List<VacancyDto> filteredList = embeddingService.filterSimilar(vacancyDtoList, score);

        log.info("Отфильтрованный список c релевантными вакансиями: {}", filteredList);

        return filteredList;
    }
}
