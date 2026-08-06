package com.zhevlakov.findjobtelegrambot.fsm.handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.zhevlakov.findjobtelegrambot.fsm.FsmHandler;
import com.zhevlakov.findjobtelegrambot.fsm.FsmStates;
import com.zhevlakov.findjobtelegrambot.user.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class AskCityFsmHandler implements FsmHandler {
    @Override
    public AbstractSendRequest<?> handle(
            Message message,
            UserEntity user
    ) {
        return null;
    }

    @Override
    public UserEntity changeState(UserEntity user) {
        return null;
    }

    @Override
    public FsmStates getState() {
        return null;
    }
}
