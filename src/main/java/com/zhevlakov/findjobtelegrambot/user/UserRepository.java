package com.zhevlakov.findjobtelegrambot.user;

import com.zhevlakov.findjobtelegrambot.fsm.FsmStates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByChatIdAndState(Long chatId, FsmStates state);
}
