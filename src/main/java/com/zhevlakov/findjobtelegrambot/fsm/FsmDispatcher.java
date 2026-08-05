package com.zhevlakov.findjobtelegrambot.fsm;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.zhevlakov.findjobtelegrambot.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FsmDispatcher {
    private final Map<FsmStates, FsmHandler> fsmHandlerMap;

    @Autowired
    public FsmDispatcher(
            List<FsmHandler> fsmHandlers
    ) {
        this.fsmHandlerMap = fsmHandlers.stream()
                .collect(Collectors.toMap(
                        FsmHandler::getState,
                        Function.identity(),
                        (existing, replacement) -> existing,
                        HashMap::new
                ));
    }

    public AbstractSendRequest<?> processFsmCommand(UserEntity user, Update update) {
        var status = user.getState();
        var fsmHandler = fsmHandlerMap.get(status);
        if (fsmHandler == null) {
            throw new IllegalArgumentException("Такой комманды нет в списке.");
        }

        return fsmHandler.handle(update, user);
    }
}
