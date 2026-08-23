package com.zhevlakov.findjobtelegrambot.callback;

import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;

public interface CallbackHandler {
    BotResponse handle(CallbackContent callbackContent);
    InlineDataCode queryCode();
}
