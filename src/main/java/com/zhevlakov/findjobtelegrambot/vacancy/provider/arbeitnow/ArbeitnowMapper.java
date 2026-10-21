package com.zhevlakov.findjobtelegrambot.vacancy.provider.arbeitnow;

import com.zhevlakov.findjobtelegrambot.vacancy.VacancyDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ArbeitnowMapper {
    private final static String WEBSITE_NAME = "Arbeitnow";

    public VacancyDto toDto(ArbeitnowJob job) {
        return new VacancyDto(
                job.title(),
                job.url(),
                job.companyName(),
                job.description(),
                null,
                WEBSITE_NAME,
                LocalDateTime.now()
        );
    }
}
