package com.zhevlakov.findjobtelegrambot.fsm.handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.zhevlakov.findjobtelegrambot.fsm.FsmHandler;
import com.zhevlakov.findjobtelegrambot.fsm.FsmStates;
import com.zhevlakov.findjobtelegrambot.user.UserEntity;
import com.zhevlakov.findjobtelegrambot.user.UserService;
import com.zhevlakov.findjobtelegrambot.user.query.UserQueryRequestSupplier;
import org.springframework.stereotype.Component;

@Component
public class FreeFsmHandler implements FsmHandler {
    private final static String requestMessage = "Введите желаемую должность:";

    private final UserService userService;
    private final UserQueryRequestSupplier requestSupplier;

    public FreeFsmHandler(
            UserService userService,
            UserQueryRequestSupplier requestSupplier
    ) {
        this.userService = userService;
        this.requestSupplier = requestSupplier;
    }

    @Override
    public AbstractSendRequest<?> handle(
        Message message,
        UserEntity user
    ) {
        changeState(user);
        var request = requestSupplier.getRequest(user, requestMessage, FsmStates.ASK_POSITION);
        userService.updateUser(user);
        return request;
    }

    public UserEntity changeState(UserEntity user) {
        user.setState(FsmStates.ASK_POSITION);
        return user;
    }

    @Override
    public FsmStates getState() {
        return FsmStates.FREE;
    }
}
