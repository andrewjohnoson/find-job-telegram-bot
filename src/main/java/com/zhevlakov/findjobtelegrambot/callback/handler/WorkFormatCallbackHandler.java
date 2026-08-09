package com.zhevlakov.findjobtelegrambot.callback.handler;

import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.callback.CallbackContent;
import com.zhevlakov.findjobtelegrambot.callback.CallbackHandler;
import com.zhevlakov.findjobtelegrambot.fsm.FsmStateCode;
import org.springframework.stereotype.Component;

@Component
public class WorkFormatCallbackHandler implements CallbackHandler {
    @Override
    public BotResponse handle(CallbackContent callbackContent) {
        return null;
    }

    @Override
    public FsmStateCode queryCode() {
        return FsmStateCode.ASK_WORK_FORMAT;
    }
}
