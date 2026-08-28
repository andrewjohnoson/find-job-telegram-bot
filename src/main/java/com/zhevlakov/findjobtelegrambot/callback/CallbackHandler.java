package com.zhevlakov.findjobtelegrambot.callback;

import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;

import java.util.List;

public interface CallbackHandler {
    List<BotResponse> handle(CallbackContent callbackContent);
    InlineDataCode queryCode();
}
