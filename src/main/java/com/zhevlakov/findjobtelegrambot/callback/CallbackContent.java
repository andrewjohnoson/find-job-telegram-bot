package com.zhevlakov.findjobtelegrambot.callback;

public record CallbackContent(
        Long chatId,
        String data,
        String queryStepCode,
        String code
) {
}
