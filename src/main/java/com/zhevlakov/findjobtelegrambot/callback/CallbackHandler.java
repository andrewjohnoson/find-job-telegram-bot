package com.zhevlakov.findjobtelegrambot.callback;

import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.fsm.FsmStateCode;

public interface CallbackHandler {
    BotResponse handle(CallbackContent callbackContent);
    FsmStateCode queryCode();
}
