package com.zhevlakov.findjobtelegrambot.vacancy;

import com.zhevlakov.findjobtelegrambot.user.vacancy.UserVacancyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class VacancyService {
    private static final int PAGE_SIZE = 10;
    private static final int PAGE_NUM = 0;

    private final VacancyRepository vacancyRepository;

    private final UserVacancyService userVacancyService;
    private final Logger log = LoggerFactory.getLogger(VacancyService.class);


    public VacancyService(VacancyRepository vacancyRepository,
                          UserVacancyService userVacancyService
    ) {
        this.vacancyRepository = vacancyRepository;
        this.userVacancyService = userVacancyService;
    }

    public List<Vacancy> getVacanciesByFilter(
            VacancySearchFilter filter
    ) {
        int pageSize = filter.pageSize() != null ? filter.pageSize() : PAGE_SIZE;
        int pageNum = filter.pageNum() != null ? filter.pageNum() : PAGE_NUM;

        Pageable pager = Pageable
                .ofSize(pageSize)
                .withPage(pageNum);


//        return vacancyRepository.searchAllByFilter(pager);
        return null;
    }

    public void saveNewVacancies(Long userId, List<Vacancy> vacancies) {
        if (vacancies == null) {
            return;
        }

        List<String> urls = vacancies.stream()
                .map(Vacancy::getUrl)
                .toList();

        List<Vacancy> existingVacancies = vacancyRepository.findByUrlIn(urls);

        Map<String, Vacancy> existingVacanciesMap = existingVacancies.stream()
                .collect(Collectors.toMap(Vacancy::getUrl, Function.identity()));

        List<Vacancy> vacanciesToSave = new ArrayList<>();

        log.info("Новые вакансии: {}", vacanciesToSave);

        for (Vacancy vacancy : vacancies) {
            Vacancy existing = existingVacanciesMap.get(vacancy.getUrl());

            if (existing == null) {
                vacanciesToSave.add(vacancy);
            }
        }

        if (!vacanciesToSave.isEmpty()) {
            vacancyRepository.saveAll(vacanciesToSave);
            userVacancyService.linkVacancies(userId, vacanciesToSave);
            log.info("Вакансии сохранены в БД.");
        }
    }
}
