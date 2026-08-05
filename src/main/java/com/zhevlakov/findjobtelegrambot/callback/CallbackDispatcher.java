package com.zhevlakov.findjobtelegrambot.callback;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import org.springframework.stereotype.Component;

@Component
public class CallbackDispatcher {
    public AbstractSendRequest<?> processCallback(CallbackQuery callback) {
        return null;
    }
}
