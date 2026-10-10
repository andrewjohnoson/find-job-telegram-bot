package com.zhevlakov.findjobtelegrambot.vacancy;

import com.zhevlakov.findjobtelegrambot.vacancy.provider.remotive.RemotiveVacancyProvider;
import jakarta.transaction.Transactional;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VacancyManager {
    private final Logger log = LoggerFactory.getLogger(VacancyManager.class);

    // Поставщики вакансий
    private final RemotiveVacancyProvider remotiveVacancyProvider;

    // Сервисы
    private final VacancyService vacancyService;

    private final VacancyFormatter vacancyFormatter;

    public VacancyManager(
            RemotiveVacancyProvider remotiveVacancyProvider,
            VacancyService vacancyService,
            VacancyFormatter vacancyFormatter
    ) {
        this.remotiveVacancyProvider = remotiveVacancyProvider;
        this.vacancyService = vacancyService;
        this.vacancyFormatter = vacancyFormatter;
    }

    @Transactional
    public void fetch(Long userId) {
        List<VacancyDto> vacancyDtoList = new ArrayList<>();

        List<VacancyDto> remotiveList = remotiveVacancyProvider.fetch(userId);
        vacancyDtoList.addAll(remotiveList);

        List<Vacancy> vacancies = vacancyDtoList.stream()
                        .map(dto -> {
                            String fixedDescription = vacancyFormatter.parseHtml(dto.description());

                            Vacancy vacancy = new Vacancy();
                            vacancy.setTitle(dto.title());
                            vacancy.setDescription(fixedDescription);
                            vacancy.setCompany(dto.company());
                            vacancy.setSalary(dto.salary());
                            vacancy.setUrl(dto.url());
                            vacancy.setPublicationDate(dto.publicationDate());

                            return vacancy;
                        }).toList();

        syncWithDatabase(userId, vacancies);
    }

    @Transactional
    protected void syncWithDatabase(Long userId, List<Vacancy> vacancyDtoList) {
        vacancyService.saveNewVacancies(userId, vacancyDtoList);
        log.info("Вакансии синхронизованы.");
    }
}
