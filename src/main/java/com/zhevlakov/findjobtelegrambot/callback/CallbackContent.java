package com.zhevlakov.findjobtelegrambot.callback;

public record CallbackContent(
        Long chatId,
        String data,
        String queryStateCode,
        String code
) {
}
