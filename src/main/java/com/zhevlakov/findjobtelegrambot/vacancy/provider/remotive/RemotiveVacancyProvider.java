package com.zhevlakov.findjobtelegrambot.vacancy.provider.remotive;

import com.zhevlakov.findjobtelegrambot.user.query.UserQuery;
import com.zhevlakov.findjobtelegrambot.user.query.UserQueryService;
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

@Component
public class RemotiveVacancyProvider implements VacancyProvider {
    private final Logger log = LoggerFactory.getLogger(RemotiveVacancyProvider.class);
    private final RestClient restClient;
    private final UserQueryService userQueryService;
    private final RemotiveMapper remotiveMapper;
    private final VacancyFilter vacancyFilter;

    public RemotiveVacancyProvider(
            UserQueryService userQueryService,
            RemotiveMapper remotiveMapper,
            VacancyFilter vacancyFilter
    ) {
        this.userQueryService = userQueryService;
        this.restClient = RestClient.builder()
                .baseUrl("https://remotive.com/api")
                .defaultHeader("User-Agent", "FindJobTelegramBot/1.0")
                .build();
        this.remotiveMapper = remotiveMapper;
        this.vacancyFilter = vacancyFilter;
    }

    @Override
    public List<VacancyDto> fetch(Long userId) {
        RemotiveResponse response = getVacancies(userId);

        if (response == null) {
            log.error("Не удлаось получить вакансии с Remotive.");
            return null;
        }

        List<VacancyDto> vacancyDtoList = response.jobs().stream()
                .map(remotiveMapper::toDto)
                .toList();

        log.info("Был сформирован список DTO-объектов вакансий: {}", vacancyDtoList);

        return vacancyFilter.filterVacancy(vacancyDtoList, null, userId);
    }

    private RemotiveResponse getVacancies(Long userId) {
        UserQuery userQuery = userQueryService.getByChatId(userId);
        String search = userQuery.getPosition();

        String uri = UriComponentsBuilder.fromPath("/remote-jobs")
                .queryParam("search", search)
                .toUriString();

        log.info("URI запроса: {}", uri);

        try {
            RemotiveResponse response = restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(RemotiveResponse.class);

            if (response == null || response.jobs() == null || response.jobs().isEmpty()) {
                log.error("С сайта Remotive список работ не получен. Запрос: {}", search);
                return null;
            }

            return response;
        } catch (RestClientResponseException e) {
            log.error("HTTP ошибка {} при запросе страницы с Remotive: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            return null;
        } catch (RestClientException e) {
            log.error("Ошибка клиента/парсинга с Remotive: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("Неизвестная ошибка при загрузке {}", e);
            return null;
        }
    }
}