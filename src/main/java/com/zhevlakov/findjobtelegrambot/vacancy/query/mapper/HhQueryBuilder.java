package com.zhevlakov.findjobtelegrambot.vacancy.query.mapper;

import com.zhevlakov.findjobtelegrambot.user.query.UserQuery;
import com.zhevlakov.findjobtelegrambot.vacancy.query.QueryBuilder;
import com.zhevlakov.findjobtelegrambot.vacancy.query.mapper.hh.AreaDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class HhQueryBuilder implements QueryBuilder {
    private final PlatformConfig platformConfig;
    private Map<String, String> citiesMap;
    private final Logger log = LoggerFactory.getLogger(HhQueryBuilder.class);

    public HhQueryBuilder(VacancyProperties properties) {
        this.platformConfig = properties.platform().get("hh");
    }

    @Override
    public String buildQuery(UserQuery userQuery) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(platformConfig.baseUrl());

        applyParamMapping(
                uriBuilder,
                platformConfig.queryParamNames().get("position"),
                userQuery.getPosition()
        );

        applyParamMapping(
                uriBuilder,
                platformConfig.queryParamNames().get("salary"),
                userQuery.getSalary()
        );

        return null;
    }

    private void applyParamMapping(
            UriComponentsBuilder uriBuilder,
            String paramName,
            String value
    ) {

    }

    private void applyParamMapping(
            UriComponentsBuilder uriBuilder,
            String paramName,
            Map<String, String> mapper,
            Set<String> values
    ) {

    }

    private String getCityCode(String city) {
        RestClient restClient = RestClient.builder()
                .baseUrl(platformConfig.baseUrl())
                .defaultHeader("User-Agent", "FindJobTelegramBot/1.0 (andreyzhevlakov23@gmail.com)")
                .build();

        List<AreaDto> areaDtoList = restClient.get()
                .uri("/areas")
                .retrieve()
                .body(new ParameterizedTypeReference<List<AreaDto>>() {});

        if (areaDtoList.isEmpty()) {
            throw new NullPointerException();
        }

        List<AreaDto> cities = new ArrayList<>();
        collectCities(areaDtoList, cities);
        return "";
    }

    public void test() {
        log.info(getCityCode("h"));
    }

    private void collectCities(List<AreaDto> areaDtoList, List<AreaDto> result) {
        for (AreaDto area : areaDtoList) {
            if (area.areas().isEmpty()) {
                result.add(area);
                return;
            }
            collectCities(area.areas(), result);
        }
    }
}
