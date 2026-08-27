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
        if (!data.contains(":")) {
            return extractCode(data);
        }
        var inlineDataCode = data.substring(0, data.indexOf(':'));
        return !inlineDataCode.isEmpty() ? inlineDataCode : extractCode(data);
    }

    private String extractCode(String data) {
        var hasDash = data.contains("-");
        return hasDash ?
                data.substring(data.indexOf(':') + 1, data.indexOf('-')) :
                data.substring(data.indexOf(':') + 1);
    }

    private Long extractAdditionalId(String data) {
        return data.contains("-") ? Long.parseLong(data.substring(data.indexOf('-') + 1)) : null;
    }
}
