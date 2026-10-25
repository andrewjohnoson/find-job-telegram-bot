package com.zhevlakov.findjobtelegrambot.vacancy;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VacancySyncService {
    private final VacancyService vacancyService;
    private final Logger log = LoggerFactory.getLogger(VacancySyncService.class);

    public VacancySyncService(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    @Transactional
    public void syncWithDatabase(Long userId, List<Vacancy> vacancyDtoList) {
        vacancyService.saveNewVacancies(userId, vacancyDtoList);
        log.info("Вакансии синхронизованы.");
    }
}
