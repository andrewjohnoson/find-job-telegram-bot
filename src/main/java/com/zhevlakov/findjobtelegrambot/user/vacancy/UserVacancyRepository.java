package com.zhevlakov.findjobtelegrambot.user.vacancy;

import com.zhevlakov.findjobtelegrambot.vacancy.VacancyStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserVacancyRepository extends JpaRepository<UserVacancy, Long> {
    @Query("""
        select uv from UserVacancy uv
        left join fetch uv.vacancy
        where uv.user.chatId = :user_id
                and uv.status <> :status
        """)
    List<UserVacancy> findAllVisibleByUser(
            @Param("user_id") Long userId,
            @Param("status") VacancyStatus status,
            Pageable pager
        );

    @Transactional
    UserVacancy getUserVacancyByVacancy_Id(Long vacancyId);
}
