package com.zhevlakov.findjobtelegrambot.vacancy.provider;

import com.zhevlakov.findjobtelegrambot.user.query.UserQuery;
import com.zhevlakov.findjobtelegrambot.user.query.UserQueryService;
import com.zhevlakov.findjobtelegrambot.vacancy.VacancyDto;
import com.zhevlakov.findjobtelegrambot.vacancy.playwrightscrapper.HhPlaywrightScrapper;
import com.zhevlakov.findjobtelegrambot.vacancy.query.mapper.HhQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class HhVacancyProvider implements VacancyProvider{
    private final RestClient restClient = RestClient.create();
    private final Logger log = LoggerFactory.getLogger(HhVacancyProvider.class);
    private final HhPlaywrightScrapper hhPlaywrightScrapper;
    private final HhQueryBuilder hhQueryBuilder;
    private final UserQueryService userQueryService;

    @Autowired
    public HhVacancyProvider(
            HhPlaywrightScrapper hhPlaywrightScrapper,
            HhQueryBuilder hhQueryBuilder,
            UserQueryService userQueryService
    ) {
        this.hhPlaywrightScrapper = hhPlaywrightScrapper;
        this.hhQueryBuilder = hhQueryBuilder;
        this.userQueryService = userQueryService;
    }

    public List<VacancyDto> fetch(Long userId) {
        UserQuery userQuery = userQueryService.getByChatId(userId);
        String query = hhQueryBuilder.buildQuery(userQuery).toString();
        String userVacancySelector = "[data-qa=\"vacancy-serp__vacancy\"]";



        return hhPlaywrightScrapper.fetchVacancies(query, userVacancySelector);
    }
}
