package com.zhevlakov.findjobtelegrambot.vacancy;

public record VacancyDto(
        String title,
        String url,
        String company,
        String description,
        String salary,
        String jobHuntingWebsite,
        String publicationDate
) {
}
