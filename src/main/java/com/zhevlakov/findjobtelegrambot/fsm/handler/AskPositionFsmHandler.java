package com.zhevlakov.findjobtelegrambot.fsm.handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.zhevlakov.findjobtelegrambot.fsm.FsmStates;
import com.zhevlakov.findjobtelegrambot.user.UserEntity;
import com.zhevlakov.findjobtelegrambot.fsm.FsmHandler;
import org.springframework.stereotype.Component;

@Component
public class AskPositionFsmHandler implements FsmHandler {
    @Override
    public AbstractSendRequest<?> handle(
            Message message,
            UserEntity user
    ) {
        return null;
    }

    @Override
    public AbstractSendRequest<?> changeStateAndSendRequest(UserEntity user) {
        return null;
    }

    @Override
    public FsmStates getState() {
        return null;
    }
}
