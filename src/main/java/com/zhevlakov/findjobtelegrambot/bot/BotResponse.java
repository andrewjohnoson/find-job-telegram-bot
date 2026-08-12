package com.zhevlakov.findjobtelegrambot.bot;

import com.pengrad.telegrambot.model.request.Keyboard;

public record BotResponse(
        Long chatId,
        String text,
        Keyboard keyboard,
        Boolean removeKeyboard
) {
    public static BotResponse post(Long chatId, String text) {
        return new BotResponse(chatId, text, null, false);
    }

    public static BotResponse post(Long chatId, String text, Keyboard keyboard) {
        return new BotResponse(chatId, text, keyboard, false);
    }

    public static BotResponse post(Long chatId, String text, Keyboard keyboard, Boolean removeKeyboard) {
        return new BotResponse(chatId, text, keyboard, removeKeyboard);
    }

    public static BotResponse error(Long chatId, String errorText) {
        return new BotResponse(chatId, errorText, null, false);
    }

    public static BotResponse error(Long chatId, String errorText, Keyboard keyboard) {
        return new BotResponse(chatId, errorText, keyboard, false);
    }
}
