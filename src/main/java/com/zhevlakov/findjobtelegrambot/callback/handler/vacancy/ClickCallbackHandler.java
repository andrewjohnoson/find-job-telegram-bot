package com.zhevlakov.findjobtelegrambot.callback.handler.vacancy;

import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.callback.CallbackContent;
import com.zhevlakov.findjobtelegrambot.callback.CallbackHandler;
import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClickCallbackHandler implements CallbackHandler {
    @Override
    public List<BotResponse> handle(CallbackContent callbackContent) {
        return null;
    }

    @Override
    public InlineDataCode queryCode() {
        return InlineDataCode.VACANCY_CLICK;
    }
}
