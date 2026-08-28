package com.zhevlakov.findjobtelegrambot.bot;

import com.pengrad.telegrambot.model.request.Keyboard;

import java.util.List;

public record
BotResponse(
        Long chatId,
        String text,
        Keyboard keyboard,
        Boolean removeKeyboard,
        Boolean updatePrevKeyboard,
        Boolean removePost
) {
    public static BotResponse post(Long chatId, String text) {
        return new BotResponse(chatId, text, null, false, false, false);
    }

    public static BotResponse post(Long chatId, String text, Keyboard keyboard) {
        return new BotResponse(chatId, text, keyboard, false, false, false);
    }

    public static BotResponse post(Long chatId, String text, Keyboard keyboard, Boolean removeKeyboard) {
        return new BotResponse(chatId, text, keyboard, removeKeyboard, false, false);
    }

    public static BotResponse post(Long chatId, String text, Keyboard keyboard, Boolean removeKeyboard, Boolean updatePrevKeyboard) {
        return new BotResponse(chatId, text, keyboard, removeKeyboard, updatePrevKeyboard, false);
    }

    public static BotResponse post(Long chatId, String text, Keyboard keyboard, Boolean removeKeyboard, Boolean updatePrevKeyboard, Boolean removePost) {
        return new BotResponse(chatId, text, keyboard, removeKeyboard, updatePrevKeyboard, removePost);
    }

    public static BotResponse error(Long chatId, String errorText) {
        return new BotResponse(chatId, errorText, null, false, false, false);
    }

    public static BotResponse error(Long chatId, String errorText, Keyboard keyboard) {
        return new BotResponse(chatId, errorText, keyboard, false, false, false);
    }

    public static List<BotResponse> asList(BotResponse response) {
        return List.of(response);
    }
}
