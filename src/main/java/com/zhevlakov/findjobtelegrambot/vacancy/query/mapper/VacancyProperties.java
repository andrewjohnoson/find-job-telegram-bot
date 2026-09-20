package com.zhevlakov.findjobtelegrambot.vacancy.query.mapper;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "vacancies")
public record VacancyProperties(
        Map<String, PlatformConfig> platforms
) {
}
