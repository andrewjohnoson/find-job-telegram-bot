package com.zhevlakov.findjobtelegrambot.fsm.handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.zhevlakov.findjobtelegrambot.fsm.FsmStates;
import com.zhevlakov.findjobtelegrambot.user.UserEntity;
import com.zhevlakov.findjobtelegrambot.fsm.FsmHandler;
import com.zhevlakov.findjobtelegrambot.user.query.UserQueryRequestSupplier;
import com.zhevlakov.findjobtelegrambot.user.query.UserQueryService;
import com.zhevlakov.findjobtelegrambot.user.query.UserQueryValidator;
import com.zhevlakov.findjobtelegrambot.user.UserService;
import org.springframework.stereotype.Component;

@Component
public class AskPositionFsmHandler implements FsmHandler {
    private final UserService userService;
    private final UserQueryService queryService;
    private final UserQueryValidator requestValidator;
    private final UserQueryRequestSupplier requestSupplier;

    private final static String requestMessage = "Введите опыт работы:";

    public AskPositionFsmHandler(
            UserService userService,
            UserQueryService queryService,
            UserQueryValidator requestValidator,
            UserQueryRequestSupplier requestSupplier
    ) {
        this.userService = userService;
        this.queryService = queryService;
        this.requestValidator = requestValidator;
        this.requestSupplier = requestSupplier;
    }

    @Override
    public AbstractSendRequest<?> handle(
            Message message,
            UserEntity user
    ) {
        var chatId = message.chat().id();
        var position = message.text();
        if (!requestValidator.isPosition(position)) {
            return new SendMessage(chatId, "Должность не должна содержать цифры. Повторите ввод.");
        }

        var query = queryService.getById(chatId);
        query.setPosition(position);
        queryService.updateQuery(query);

        changeState(user);
        var request = requestSupplier.getRequest(user, requestMessage, FsmStates.ASK_EXPERIENCE);
        userService.updateUser(user);
        return request;
    }

    @Override
    public UserEntity changeState(UserEntity user) {
        user.setState(FsmStates.ASK_EXPERIENCE);
        return user;
    }

    @Override
    public FsmStates getState() {
        return FsmStates.ASK_POSITION;
    }
}
