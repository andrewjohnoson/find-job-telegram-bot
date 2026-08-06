package com.zhevlakov.findjobtelegrambot.fsm.handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.zhevlakov.findjobtelegrambot.fsm.FsmHandler;
import com.zhevlakov.findjobtelegrambot.fsm.FsmStates;
import com.zhevlakov.findjobtelegrambot.user.UserEntity;
import com.zhevlakov.findjobtelegrambot.user.UserService;
import com.zhevlakov.findjobtelegrambot.user.query.UserQueryRequestSupplier;
import com.zhevlakov.findjobtelegrambot.user.query.UserQueryService;
import com.zhevlakov.findjobtelegrambot.user.query.UserQueryValidator;
import org.springframework.stereotype.Component;

@Component
public class AskWorkFormatFsmHandler implements FsmHandler {
    private final UserQueryValidator queryValidator;
    private final UserQueryRequestSupplier requestSupplier;
    private final UserQueryService queryService;
    private final UserService userService;

    private final static String requestMessage = "Введите город:";

    public AskWorkFormatFsmHandler(
            UserQueryValidator queryValidator,
            UserQueryRequestSupplier requestSupplier,
            UserQueryService queryService,
            UserService userService
    ) {
        this.queryValidator = queryValidator;
        this.requestSupplier = requestSupplier;
        this.queryService = queryService;
        this.userService = userService;
    }

    @Override
    public AbstractSendRequest<?> handle(
            Message message,
            UserEntity user
    ) {
        var chatId = message.chat().id();
        var experience = message.text();
        if (!queryValidator.isExperience(experience)) {
            return new SendMessage(chatId, "Вводится положительное целое число. Повторите ввод.");
        }

        var query = queryService.getById(chatId);
        query.setExperience(experience);
        queryService.updateQuery(query);

        changeState(user);
        var request = requestSupplier.getRequest(user, requestMessage, FsmStates.ASK_SALARY);
        userService.updateUser(user);
        return request;
    }

    @Override
    public UserEntity changeState(UserEntity user) {
        user.setState(FsmStates.ASK_SALARY);
        return user;
    }

    @Override
    public FsmStates getState() {
        return FsmStates.ASK_WORK_FORMAT;
    }
}
