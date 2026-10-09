package com.zhevlakov.findjobtelegrambot.vacancy.query.mapper;

import com.zhevlakov.findjobtelegrambot.user.query.UserQuery;
import com.zhevlakov.findjobtelegrambot.vacancy.query.QueryBuilder;
import com.zhevlakov.findjobtelegrambot.vacancy.query.mapper.hh.SuggestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.Set;

@Component
public class HhQueryBuilder implements QueryBuilder {
    private final PlatformConfig platformConfig;
    private final Logger log = LoggerFactory.getLogger(HhQueryBuilder.class);

    public HhQueryBuilder(VacancyProperties properties) {
        this.platformConfig = properties.platforms().get("hh");
    }

    @Override
    public URI buildQuery(UserQuery userQuery) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(platformConfig.baseUrl());

        applyParamMapping(uriBuilder, platformConfig.queryParamNames().get("position"), userQuery.getPosition());
        applyParamMapping(uriBuilder, platformConfig.queryParamNames().get("salary"), userQuery.getSalary());
        applyParamMapping(uriBuilder, platformConfig.queryParamNames().get("experience"),
                platformConfig.experienceMapping(), userQuery.getExperienceList());
        applyParamMapping(uriBuilder, platformConfig.queryParamNames().get("employment"),
                platformConfig.employmentMapping(), userQuery.getEmploymentTypeList());
        applyParamMapping(uriBuilder, platformConfig.queryParamNames().get("city"),
                getCityCodeViaSuggest(userQuery.getCity()));
        applyParamMapping(uriBuilder, platformConfig.queryParamNames().get("work-format"),
                        platformConfig.workFormatMapping(), userQuery.getWorkFormatList());

        if (platformConfig.additionalParams() != null && !platformConfig.additionalParams().isBlank()) {
            uriBuilder.query(platformConfig.additionalParams());
        }

        return uriBuilder
                .encode()
                .build()
                .toUri();
    }

    private void applyParamMapping(
            UriComponentsBuilder uriBuilder,
            String paramName,
            String value
    ) {
        log.info("paramName={}, value={}", paramName, value);
        if (paramName == null || paramName.isBlank()) {
            log.warn("applyParamMapping(3 args): не удалось применить параметр, т.к. он null");
            return;
        }

        if (value == null || value.isBlank()) {
            log.warn("applyParamMapping(3 args): не удалось применить параметр={}, т.к. значения в запросе пользователя пусты.", paramName);
            return;
        }
        uriBuilder.queryParam(paramName, value);
    }

    private <T extends Enum<T>> void applyParamMapping(
            UriComponentsBuilder uriBuilder,
            String paramName,
            Map<T, String> mapper,
            Set<T> values
    ) {
        if (paramName == null || paramName.isBlank()) {
            log.warn("applyParamMapping(4 args): не удалось применить параметр, т.к. он null");
            return;
        }

        if (values == null) {
            log.warn("applyParamMapping(4 args): не удалось применить параметр={}, т.к. значения в запросе пользователя пусты.", paramName);
            return;
        }

        for (T value : values) {
            String platformValue = mapper.get(value);
            if (platformValue != null) {
                uriBuilder.queryParam(paramName, platformValue);
            }
        }
    }

    private String getCityCodeViaSuggest(String city) {
        if (city == null || city.isBlank()) {
            return null;
        }

        RestClient restClient = RestClient.builder()
                .baseUrl(platformConfig.apiUrl())
                .defaultHeader("User-Agent", "FindJobTelegramBot/1.0 (andreyzhevlakov23@gmail.com)")
                .build();

        try {
            SuggestResponse response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/suggests/areas")
                            .queryParam("text", city)
                            .build())
                    .retrieve()
                    .body(SuggestResponse.class);

            if (response != null && response.items() != null && !response.items().isEmpty()) {
                return response.items().getFirst().id();
            }
        } catch (Exception e) {
            log.error("Ошибка при поиске города: {}", city, e);
        }

        return null;
    }
}
