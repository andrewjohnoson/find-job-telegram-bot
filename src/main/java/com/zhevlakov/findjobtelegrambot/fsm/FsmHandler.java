package com.zhevlakov.findjobtelegrambot.fsm;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.zhevlakov.findjobtelegrambot.user.UserEntity;
import org.springframework.stereotype.Component;

@Component
public interface FsmHandler {
    AbstractSendRequest<?> handle(Update update, UserEntity userEntity);
    FsmStates getState();
}
