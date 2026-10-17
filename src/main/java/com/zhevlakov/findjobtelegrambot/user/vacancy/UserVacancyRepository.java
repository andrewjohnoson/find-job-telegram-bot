package com.zhevlakov.findjobtelegrambot.user.vacancy;

import com.zhevlakov.findjobtelegrambot.vacancy.VacancyStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query(
            """
            select uv from UserVacancy uv
            left join fetch uv.vacancy
            where uv.isViewed = false
                    and uv.user.userId = :user_id
                    and uv.status = FREE
            order by uv.vacancy.publicationDate desc
            limit 5
            """
    )
    List<UserVacancy> getNewUserVacancies(
            @Param("user_id") Long userId
    );

    @Modifying(clearAutomatically = true)
    @Query(
        """
        update UserVacancy uv
        set uv.isViewed = true
        where uv.id in :ids
        """
    )
    void markAsViewed(@Param("ids") List<Long> userVacancyIds);

    @Query(
            """
            select uv from UserVacancy uv
            where uv.user.userId = :userId
                    and uv.vacancy.id in :vacancyIds
                    and uv.isViewed = false
            """
    )
    List<UserVacancy> findUnviewedVacancies(
            @Param("userId") Long userId,
            @Param("vacancyIds") List<Long> vacancyIds
    );

    UserVacancy getUserVacancyByVacancy_Id(Long vacancyId);
    boolean existsByIdAndStatus(Long vacancyId, VacancyStatus status);
}
