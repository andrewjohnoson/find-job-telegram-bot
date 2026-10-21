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
        join fetch uv.vacancy
        where uv.isViewed = false
                and uv.user.userId = :user_id
                and uv.status = :status
        order by uv.vacancy.publicationDate desc
        limit :limit
        """
    )
    List<UserVacancy> getLimitedUserVacanciesByStatus(
            @Param("user_id") Long userId,
            @Param("status") VacancyStatus status,
            @Param("limit") int limit
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

    @Modifying(clearAutomatically = true)
    @Query(
        """
        update UserVacancy uv
        set uv.isViewed = false
        where uv.user.userId = :userId
            and uv.status = :status
        """)
    void updateToNotViewed(
            @Param("userId") Long userId,
            @Param("status") VacancyStatus status
    );

    UserVacancy getUserVacancyByVacancy_Id(Long vacancyId);
    boolean existsByIdAndStatus(Long vacancyId, VacancyStatus status);
}
