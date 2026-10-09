package com.zhevlakov.findjobtelegrambot.user.vacancy;

import com.zhevlakov.findjobtelegrambot.vacancy.VacancyStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserVacancyRepository extends JpaRepository<UserVacancy, Long> {
    @Query("""
        select uv from UserVacancy uv
        left join fetch uv.vacancy
        where uv.user.userId = :user_id
                and uv.status <> :status
        """)
    List<UserVacancy> findAllVisibleByUser(
            @Param("user_id") Long userId,
            @Param("status") VacancyStatus status,
            Pageable pager
        );

    @Query(
            """
        select uv from UserVacancy uv
        left join fetch uv.vacancy
        where uv.user.userId = :user_id
                and uv.status = :status
        """
    )
    List<UserVacancy> findAllByUserAndStatus(
            @Param("user_id") Long userId,
            @Param("status") VacancyStatus status,
            Pageable pager
    );

    // получить первые 5 вакансий, самые новые, где is_viewed = false
    @Query(
            """
            select uv from UserVacancy uv
            left join fetch uv.vacancy
            where uv.isViewed = false and uv.user.userId = :user_id
            order by uv.vacancy.publicationDate desc
            limit 5
            """
    )
    List<UserVacancy> getNewUserVacancies(
            @Param("user_id") Long userId
    );

    UserVacancy getUserVacancyByVacancy_Id(Long vacancyId);

    boolean existsByIdAndStatus(Long vacancyId, VacancyStatus status);
    List<UserVacancy> findByUser_UserIdAndVacancy_IdIn(Long userId, List<Long> vacancyIds);
}
