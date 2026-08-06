package com.zhevlakov.findjobtelegrambot.fsm;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.zhevlakov.findjobtelegrambot.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FsmDispatcher {
    private final UserService userService;
    private final Map<FsmStates, FsmHandler> fsmHandlerMap;

    @Autowired
    public FsmDispatcher(
            UserService userService,
            List<FsmHandler> fsmHandlers
    ) {
        this.userService = userService;
        this.fsmHandlerMap = fsmHandlers.stream()
                .collect(Collectors.toMap(
                        FsmHandler::getState,
                        Function.identity(),
                        (existing, replacement) -> existing,
                        HashMap::new
                ));
    }

    public AbstractSendRequest<?> processFsmCommand(Message message) {
        var chatId = message.chat().id();
        var user = userService.getUserById(chatId);
        var status = user.getState();
        var fsmHandler = fsmHandlerMap.get(status);
        if (fsmHandler == null) {
            throw new IllegalArgumentException("Такой комманды нет в списке.");
        }

        return fsmHandler.handle(message, user);
    }
}
