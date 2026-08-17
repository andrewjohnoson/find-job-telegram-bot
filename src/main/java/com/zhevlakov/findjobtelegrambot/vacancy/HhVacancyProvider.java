package com.zhevlakov.findjobtelegrambot.vacancy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class HhVacancyProvider {
    private final RestClient restClient = RestClient.create();
    private final Logger log = LoggerFactory.getLogger(HhVacancyProvider.class);

    public ResponseEntity<HhVacancyEntity> fetchVacancies() {
        return restClient.get()
                .uri()
    }
}
