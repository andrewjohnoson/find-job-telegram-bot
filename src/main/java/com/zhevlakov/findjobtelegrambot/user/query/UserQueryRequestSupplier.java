package com.zhevlakov.findjobtelegrambot.user.query;

import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardGenerator;
import com.zhevlakov.findjobtelegrambot.fsm.FsmStates;
import com.zhevlakov.findjobtelegrambot.user.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserQueryRequestSupplier {
    private final KeyboardGenerator keyboardGenerator;

    public UserQueryRequestSupplier(
            KeyboardGenerator keyboardGenerator
    ) {
        this.keyboardGenerator = keyboardGenerator;
    }

    public AbstractSendRequest<?> getRequest(
            UserEntity user,
            String requestMessage,
            FsmStates nextState
    ) {
        boolean hasRequest = user.getUserRequest() != null;
        if (!hasRequest) {
            user.setUserRequest(new UserQuery());
        }

        String requestText = requestMessage;

        if (hasRequest) {
            String newText = "Ваш предыдущий запрос: " + user.getUserRequest() + "\n";
            requestText = newText + requestMessage;
        }

        SendMessage request = new SendMessage(user.getChatId(), requestText);

        if (hasRequest) {
            request.replyMarkup(keyboardGenerator.getKeepPrevStateKeyboard(nextState.toString()));
        }

        return request;
    }
}
