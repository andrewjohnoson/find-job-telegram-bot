package com.zhevlakov.findjobtelegrambot.vacancy.provider.remotive;

import com.zhevlakov.findjobtelegrambot.vacancy.VacancyDto;
import org.springframework.stereotype.Component;

@Component
public class RemotiveMapper {
    private final static String WEBSITE_NAME = "Remotive";

    public VacancyDto toDto(RemotiveJob job) {
        return new VacancyDto(
                job.title(),
                job.url(),
                job.companyName(),
                job.description(),
                job.salary(),
                WEBSITE_NAME,
                job.publicationDate()
        );
    }
}
