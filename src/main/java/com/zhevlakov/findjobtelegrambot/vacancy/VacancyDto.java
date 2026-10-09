package com.zhevlakov.findjobtelegrambot.vacancy;

import java.time.LocalDateTime;

public record VacancyDto(
        String title,
        String url,
        String company,
        String description,
        String salary,
        String jobHuntingWebsite,
        LocalDateTime publicationDate
) {
}
