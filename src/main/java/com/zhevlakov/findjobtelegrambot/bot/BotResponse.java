package com.zhevlakov.findjobtelegrambot.bot;

import com.pengrad.telegrambot.model.request.Keyboard;

public record BotResponse(
        Long chatId,
        String text,
        Keyboard keyboard
) {
    public static BotResponse post(Long chatId, String text) {
        return new BotResponse(chatId, text, null);
    }

    public static BotResponse post(Long chatId, String text, Keyboard keyboard) {
        return new BotResponse(chatId, text, keyboard);
    }

    public static BotResponse error(Long chatId, String errorText) {
        return new BotResponse(chatId, errorText, null);
    }

    public static BotResponse error(Long chatId, String errorText, Keyboard keyboard) {
        return new BotResponse(chatId, errorText, keyboard);
    }
}
