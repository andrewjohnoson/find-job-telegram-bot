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
                extractInlineDataCode(data),
                extractCode(data),
                extractAdditionalId(data)
        );
    }

    private String extractInlineDataCode(String data) {
        return data.substring(0, data.indexOf(':'));
    }

    private String extractCode(String data) {
        var hasUnderline = data.contains("_");
        return hasUnderline ?
                data.substring(data.indexOf(':') + 1, data.indexOf('_')) :
                data.substring(data.indexOf(':') + 1);
    }

    private Long extractAdditionalId(String data) {
        return data.contains("_") ? Long.parseLong(data.substring(data.indexOf('_') + 1)) : null;
    }
}
