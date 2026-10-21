package com.zhevlakov.findjobtelegrambot.user;

import com.zhevlakov.findjobtelegrambot.fsm.FsmStates;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUserIdAndState(Long chatId, FsmStates state);

    @Modifying
    @Transactional
    @Query("""
        update UserEntity u
        set u.state = :state
        where u.userId = :chatId
    """)
    int updateState(@Param("chatId") Long chatId, @Param("state") FsmStates state);
}
