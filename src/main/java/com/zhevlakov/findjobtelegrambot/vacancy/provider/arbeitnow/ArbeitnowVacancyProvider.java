package com.zhevlakov.findjobtelegrambot.vacancy.provider.arbeitnow;

import com.zhevlakov.findjobtelegrambot.vacancy.VacancyDto;
import com.zhevlakov.findjobtelegrambot.vacancy.VacancyFilter;
import com.zhevlakov.findjobtelegrambot.vacancy.provider.VacancyProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@Component
public class ArbeitnowVacancyProvider implements VacancyProvider {
    private final Logger log = LoggerFactory.getLogger(ArbeitnowVacancyProvider.class);
    private final RestClient restClient;
    private final VacancyFilter vacancyFilter;
    private final ArbeitnowMapper arbeitnowMapper;

    public ArbeitnowVacancyProvider(
            VacancyFilter vacancyFilter,
            ArbeitnowMapper arbeitnowMapper
    ) {
        this.vacancyFilter = vacancyFilter;
        this.arbeitnowMapper = arbeitnowMapper;
        this.restClient = RestClient.builder()
                .baseUrl("https://www.arbeitnow.com/api")
                .defaultHeader("User-Agent", "FindJobTelegramBot/1.0")
                .build();
    }

    @Override
    public List<VacancyDto> fetch(Long userId) {
        int startPage = 1;
        int endPage = 5;

        List<CompletableFuture<ArbeitnowResponse>> futures =
                IntStream.rangeClosed(startPage, endPage)
                        .mapToObj(page ->
                                CompletableFuture.supplyAsync(() ->
                                        getVacancies(page)))
                        .toList();

        List<VacancyDto> vacancyDtoList = futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .filter(response -> response.jobs() != null)
                .flatMap(response -> response.jobs().stream())
                .map(arbeitnowMapper::toDto)
                .toList();

        log.info("Вакансии с Arbeitnow: {}", vacancyDtoList);

        if (vacancyDtoList.isEmpty()) {
            log.error("Не удалось получить вакансии с Arbeitnow.");
            return null;
        }

        return vacancyFilter.filterVacancy(vacancyDtoList, null, userId);
    }

    private ArbeitnowResponse getVacancies(int page) {
        String uri = UriComponentsBuilder.fromPath("/job-board-api")
                .queryParam("page", page)
                .toUriString();

        log.info("URI запроса: {}", uri);

        try {
            ArbeitnowResponse response = restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(ArbeitnowResponse.class);

            if (response == null || response.jobs() == null || response.jobs().isEmpty()) {
                log.error("С сайта Arbeitnow список работа со страницы = {} не получен.", page);
                return null;
            }

            return response;
        } catch (RestClientResponseException e) {
            log.error("HTTP ошибка {} при запросе страницы {} с Arbeitnow: {}",
                    e.getStatusCode(), page, e.getResponseBodyAsString());
            return null;
        } catch (RestClientException e) {
            log.error("Ошибка клиента/парсинга на странице {} с Arbeitnow: {}", page, e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("Неизвестная ошибка при загрузке страницы {}", page, e);
            return null;
        }
    }
}
