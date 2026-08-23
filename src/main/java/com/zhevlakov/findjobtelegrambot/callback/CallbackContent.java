package com.zhevlakov.findjobtelegrambot.callback;

public record CallbackContent(
        Long chatId,
        String data,
        String inlineDataCode,
        String code,
        Long additionalId
) {
}
