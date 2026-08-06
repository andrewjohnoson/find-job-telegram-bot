package com.zhevlakov.findjobtelegrambot.fsm;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.zhevlakov.findjobtelegrambot.user.UserEntity;
import org.springframework.stereotype.Component;

@Component
public interface FsmHandler {
    AbstractSendRequest<?> handle(Message message, UserEntity user);
    AbstractSendRequest<?> changeStateAndSendRequest(UserEntity user);
    FsmStates getState();
}
