package com.zhevlakov.findjobtelegrambot.vacancy;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VacancyService {
    private static int PAGE_SIZE = 10;
    private static int PAGE_NUM = 0;
    private final VacancyRepository vacancyRepository;

    public VacancyService(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
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
}
