package com.zhevlakov.findjobtelegrambot.user.query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserQueryRepository extends JpaRepository<UserQuery, Long> {
    @Query("""
            select distinct uq from UserQuery uq
            left join fetch uq.experienceList
            left join fetch uq.employmentTypeList
            left join fetch uq.workFormatList
            where uq.userEntity.chatId = :id
        """)
    Optional<UserQuery> findByUserEntity_ChatId(@Param("id") Long id);
    boolean existsByUserEntity_ChatId(Long id);
}
