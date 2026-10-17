package com.zhevlakov.findjobtelegrambot.vacancy.provider.arbeitnow;

import com.zhevlakov.findjobtelegrambot.vacancy.VacancyDto;
import com.zhevlakov.findjobtelegrambot.vacancy.provider.VacancyProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

public class ArbeitnowVacancyProvider implements VacancyProvider {
    private final Logger log = LoggerFactory.getLogger(ArbeitnowVacancyProvider.class);
    private final RestClient restClient;
    private final static String WEBSITE_NAME = "Arbeitnow";

    public ArbeitnowVacancyProvider(
    ) {
        this.restClient = RestClient.builder()
                .baseUrl("https://www.arbeitnow.com/api")
                .defaultHeader("User-Agent", "FindJobTelegramBot/1.0")
                .build();
    }

    @Override
    public List<VacancyDto> fetch(Long userId) {
        int startPage = 0;
        int endPage = 2;

        List<CompletableFuture<ArbeitnowResponse>> futures =
                IntStream.range(startPage, endPage)
                        .mapToObj(page ->
                                CompletableFuture.supplyAsync(() ->
                                        getVacancies(page)))
                        .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .filter(response -> response.jobs() != null)
                .flatMap(response -> response.jobs().stream())
                .map(this::toDto)
                .toList();
    }

    private ArbeitnowResponse getVacancies(int page) {
        String uri = UriComponentsBuilder.fromPath("/job-board-api")
                .queryParam("page", page)
                .toUriString();

        log.info("URI запроса: {}", uri);

        ArbeitnowResponse response = restClient.get()
                .uri(uri)
                .retrieve()
                .body(ArbeitnowResponse.class);

        if (response == null || response.jobs() == null || response.jobs().isEmpty()) {
            log.error("С сайта Arbeitnow список работа со страницы = {} не получен.", page);
            return null;
        }

        return response;
    }



    private VacancyDto toDto(ArbeitnowJob job) {
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
