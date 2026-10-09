package com.zhevlakov.findjobtelegrambot.user.vacancy;

import com.zhevlakov.findjobtelegrambot.user.UserEntity;
import com.zhevlakov.findjobtelegrambot.user.UserService;
import com.zhevlakov.findjobtelegrambot.vacancy.Vacancy;
import com.zhevlakov.findjobtelegrambot.vacancy.VacancySearchFilter;
import com.zhevlakov.findjobtelegrambot.vacancy.VacancyStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserVacancyService {
    private static int PAGE_SIZE = 10;
    private static int PAGE_NUM = 0;

    private final UserVacancyRepository userVacancyRepository;
    private final UserService userService;

    private final Logger log = LoggerFactory.getLogger(UserVacancyService.class);

    public UserVacancyService(UserVacancyRepository userVacancyRepository,
                              UserService userService
    ) {
        this.userVacancyRepository = userVacancyRepository;
        this.userService = userService;
    }

    public List<UserVacancy> getVisibleUserVacanciesByFilter(
            VacancySearchFilter filter,
            Long userId
    ) {
        Pageable pager = getPager(filter);

        return userVacancyRepository.findAllVisibleByUser(userId, VacancyStatus.HIDDEN, pager);
    }

    public boolean hasStatus(Long vacancyId, VacancyStatus status) {
        return userVacancyRepository.existsByIdAndStatus(vacancyId, status);
    }

    public List<UserVacancy> getUserVacanciesByStatus(
            VacancySearchFilter filter,
            Long userId,
            VacancyStatus status
    ) {
        Pageable pager = getPager(filter);

        return userVacancyRepository.findAllByUserAndStatus(userId, status, pager);
    }

    public UserVacancy changeVacancyStatus(
            Long vacancyId,
            VacancyStatus status
    ) {
        var vacancy = userVacancyRepository.getUserVacancyByVacancy_Id(vacancyId);
        vacancy.setStatus(status);
        return vacancy;
    }

    private Pageable getPager(VacancySearchFilter filter) {
        int pageSize = filter.pageSize() != null ? filter.pageSize() : PAGE_SIZE;
        int pageNum = filter.pageNum() != null ? filter.pageNum() : PAGE_NUM;

        return Pageable
                .ofSize(pageSize)
                .withPage(pageNum);
    }

    public void linkVacancies(Long userId, List<Vacancy> vacancies) {
        List<Long> vacancyIds = vacancies.stream().map(Vacancy::getId).toList();

        List<UserVacancy> existingUserVacancyList = userVacancyRepository.
                findByUserAndVacancy_IdIn(userId, vacancyIds);

        Set<Long> existingVacancyIds = existingUserVacancyList.stream()
                .map(vacancy -> vacancy.getVacancy().getId())
                .collect(Collectors.toSet());

        List<UserVacancy> newVacancies = new ArrayList<>();

        UserEntity user = userService.getUserById(userId);

        for (Vacancy vacancy : vacancies) {
            if (!existingVacancyIds.contains(vacancy.getId())) {
                UserVacancy userVacancy = new UserVacancy(
                        null,
                        user,
                        vacancy,
                        VacancyStatus.FREE
                );
                newVacancies.add(userVacancy);
            }
        }

        log.info("Новые вакансии для пользователя = {}: {}", userId, newVacancies);

        if (!newVacancies.isEmpty()) {
            userVacancyRepository.saveAll(newVacancies);
        }
    }
}
