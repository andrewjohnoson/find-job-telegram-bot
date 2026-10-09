package com.zhevlakov.findjobtelegrambot.vacancy.provider.remotive;

import com.zhevlakov.findjobtelegrambot.user.query.UserQuery;
import com.zhevlakov.findjobtelegrambot.user.query.UserQueryService;
import com.zhevlakov.findjobtelegrambot.vacancy.VacancyDto;
import com.zhevlakov.findjobtelegrambot.vacancy.provider.VacancyProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
public class RemotiveVacancyProvider implements VacancyProvider {
    private final Logger log = LoggerFactory.getLogger(RemotiveVacancyProvider.class);
    private final RestClient restClient;
    private final UserQueryService userQueryService;

    public RemotiveVacancyProvider(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
        this.restClient = RestClient.builder()
                .baseUrl("https://remotive.com/api")
                .defaultHeader("UserAgent", "FindJobTelegramBot/1.0")
                .build();
    }

    @Override
    public List<VacancyDto> fetch(Long userId) {
        UserQuery userQuery = userQueryService.getByChatId(userId);
        String search = userQuery.getPosition();

        String uri = UriComponentsBuilder.fromPath("/remote-jobs")
                .queryParam("search", search)
                .toUriString();

        log.info("URI запроса: {}", uri);

        RemotiveResponse response = restClient.get()
                .uri(uri)
                .retrieve()
                .body(RemotiveResponse.class);

        if (response == null || response.jobs() == null || response.jobs().isEmpty()) {
            log.error("С сайта Remotive список работ не получен. Запрос: {}", search);
            return null;
        }

        List<VacancyDto> vacancyDtoList = response.jobs().stream()
                .map(job -> new VacancyDto(
                        job.title(),
                        job.url(),
                        job.companyName(),
                        job.description(),
                        job.salary(),
                        "Remotive",
                        job.publicationDate()
                ))
                .toList();

        log.info("Был сформирован список DTO-объектов вакансий: {}", vacancyDtoList);

        return vacancyDtoList;
    }
}
