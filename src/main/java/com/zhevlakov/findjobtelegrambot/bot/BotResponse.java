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

    public static BotResponse postWithReplyKeyboard(Long chatId, String text, Keyboard replyKeyboard) {
        return new BotResponse(chatId, text, replyKeyboard);
    }

    public static BotResponse error(Long chatId, String errorText) {
        return new BotResponse(chatId, errorText, null);
    }
}
