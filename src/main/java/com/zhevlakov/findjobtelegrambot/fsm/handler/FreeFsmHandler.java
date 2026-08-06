package com.zhevlakov.findjobtelegrambot.fsm.handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.zhevlakov.findjobtelegrambot.KeyboardGenerator;
import com.zhevlakov.findjobtelegrambot.fsm.FsmHandler;
import com.zhevlakov.findjobtelegrambot.fsm.FsmStates;
import com.zhevlakov.findjobtelegrambot.user.UserEntity;
import com.zhevlakov.findjobtelegrambot.user.UserRequest;
import com.zhevlakov.findjobtelegrambot.user.UserService;
import org.springframework.stereotype.Component;

@Component
public class FreeFsmHandler implements FsmHandler {

    private final static String requestMessage = """
            Введите желаемую должность:
            """;

    private final KeyboardGenerator keyboardGenerator;
    private final UserService userService;

    public FreeFsmHandler(
            KeyboardGenerator keyboardGenerator,
            UserService userService
    ) {
        this.keyboardGenerator = keyboardGenerator;
        this.userService = userService;
    }

    @Override
    public AbstractSendRequest<?> handle(
        Message message,
        UserEntity user
    ) {
        changeState(user);
        var request = changeStateAndSendRequest(user);
        userService.updateUser(user);
        return request;
    }

    public void changeState(UserEntity user) {
        user.setState(FsmStates.ASK_POSITION);
    }

    @Override
    public AbstractSendRequest<?> changeStateAndSendRequest(UserEntity user) {
        boolean hasRequest = user.getUserRequest() != null;
        if (!hasRequest) {
            user.setUserRequest(new UserRequest());
        }

        String requestText = requestMessage;

        if (hasRequest) {
            String newText = "Ваш предыдущий запрос: " + user.getUserRequest() + "\n";
            requestText = newText + requestMessage;
        }

        SendMessage request = new SendMessage(user.getChatId(), requestText);

        if (hasRequest) {
            request.replyMarkup(keyboardGenerator.getKeepPrevStateKeyboard(FsmStates.ASK_POSITION.toString()));
        }

        return request;
    }

    @Override
    public FsmStates getState() {
        return FsmStates.FREE;
    }
}
