package com.zhevlakov.findjobtelegrambot.callback;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import org.springframework.stereotype.Component;

@Component
public class CallbackDispatcher {
    private final CallbackMapper callbackMapper;

    public CallbackDispatcher(
            CallbackMapper callbackMapper
    ) {
        this.callbackMapper = callbackMapper;
    }

    public BotResponse processCallback(CallbackQuery callback) {
        var callbackContent = callbackMapper.toContent(callback);
        return null;
    }
}
