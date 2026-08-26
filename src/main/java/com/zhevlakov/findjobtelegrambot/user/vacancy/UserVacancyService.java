package com.zhevlakov.findjobtelegrambot.user.vacancy;

import com.zhevlakov.findjobtelegrambot.vacancy.VacancySearchFilter;
import com.zhevlakov.findjobtelegrambot.vacancy.VacancyStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserVacancyService {
    private static int PAGE_SIZE = 10;
    private static int PAGE_NUM = 0;

    private final UserVacancyRepository userVacancyRepository;

    public UserVacancyService(UserVacancyRepository userVacancyRepository) {
        this.userVacancyRepository = userVacancyRepository;
    }

    public List<UserVacancy> getVisibleUserVacanciesByFilter(
            VacancySearchFilter filter,
            Long userId
    ) {
        Pageable pager = getPager(filter);

        return userVacancyRepository.findAllVisibleByUser(userId, VacancyStatus.HIDDEN, pager);
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
}
