package com.zhevlakov.findjobtelegrambot.callback;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.callback.code.QueryCode;
import com.zhevlakov.findjobtelegrambot.fsm.FsmStates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CallbackDispatcher {
    private final CallbackMapper callbackMapper;
    private final Map<String, CallbackHandler> callbackHandlersMap;
    private final Logger log = LoggerFactory.getLogger(CallbackDispatcher.class);

    @Autowired
    public CallbackDispatcher(
            List<CallbackHandler> callbackHandlers,
            CallbackMapper callbackMapper
    ) {
        this.callbackHandlersMap = callbackHandlers.stream()
                .collect(Collectors.toMap(
                        callbackHandler -> callbackHandler.queryCode().getInlineButtonCode(),
                        Function.identity(),
                        (existing, _) -> existing,
                        HashMap::new
                ));
        this.callbackMapper = callbackMapper;
    }

    public BotResponse processCallback(CallbackQuery callback) {
        var callbackContent = callbackMapper.toContent(callback);

        var handler = callbackHandlersMap.get(callbackContent.code());
        if (handler == null) {
            log.error("Нажата inline-кнопка, которой нет в списке допустимых комманд. Чат={}", callbackContent.chatId());
            return BotResponse.error(callbackContent.chatId(), "Не существует кнопки с таким кодом.");
        }

        return handler.handle(callbackContent);
    }
}
