package com.zhevlakov.findjobtelegrambot.vacancy;

import com.zhevlakov.findjobtelegrambot.vacancy.provider.remotive.RemotiveVacancyProvider;
import jakarta.transaction.Transactional;
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
    private final VacancyMapper vacancyMapper;

    public VacancyManager(
            RemotiveVacancyProvider remotiveVacancyProvider,
            VacancyService vacancyService,
            VacancyFormatter vacancyFormatter,
            VacancyMapper vacancyMapper
    ) {
        this.remotiveVacancyProvider = remotiveVacancyProvider;
        this.vacancyService = vacancyService;
        this.vacancyFormatter = vacancyFormatter;
        this.vacancyMapper = vacancyMapper;
    }

    @Transactional
    public void fetch(Long userId) {
        List<VacancyDto> vacancyDtoList = new ArrayList<>();

        List<VacancyDto> remotiveList = remotiveVacancyProvider.fetch(userId);
        vacancyDtoList.addAll(remotiveList);

        List<Vacancy> vacancies = vacancyMapper.toDomain(vacancyDtoList);

        syncWithDatabase(userId, vacancies);
    }

    @Transactional
    protected void syncWithDatabase(Long userId, List<Vacancy> vacancyDtoList) {
        vacancyService.saveNewVacancies(userId, vacancyDtoList);
        log.info("Вакансии синхронизованы.");
    }
}
