package com.zhevlakov.findjobtelegrambot.user.query;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserQueryRepository extends JpaRepository<UserQuery, Long> {
    Optional<UserQuery> findByUserEntity_ChatId(Long id);
}
