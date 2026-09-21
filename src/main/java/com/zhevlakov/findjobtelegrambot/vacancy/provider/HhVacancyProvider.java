package com.zhevlakov.findjobtelegrambot.vacancy.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class HhVacancyProvider {
    private final RestClient restClient = RestClient.create();
    private final Logger log = LoggerFactory.getLogger(HhVacancyProvider.class);

    public String fetchVacancies() {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.hh.ru")
                        .path("/vacancies")

                        .build())
                .header("User-Agent", "FindJobTelegramBot/1.0 (andreyzhevlakov23@gmail.com)")
                .retrieve()
                .body(String.class);
    }
}
