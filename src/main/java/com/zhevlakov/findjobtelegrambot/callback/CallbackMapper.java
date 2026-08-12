package com.zhevlakov.findjobtelegrambot.callback;

import com.pengrad.telegrambot.model.CallbackQuery;
import org.springframework.stereotype.Component;

@Component
public class CallbackMapper {
    public CallbackContent toContent(CallbackQuery callbackQuery) {
        var data = callbackQuery.data();
        return new CallbackContent(
                callbackQuery.from().id(),
                data,
                extractQueryStateCode(data),
                extractCode(data)
        );
    }

    private String extractQueryStateCode(String data) {
        return data.substring(0, data.indexOf(':'));
    }

    private String extractCode(String data) {
        return data.substring(data.indexOf(':') + 1);
    }
}
