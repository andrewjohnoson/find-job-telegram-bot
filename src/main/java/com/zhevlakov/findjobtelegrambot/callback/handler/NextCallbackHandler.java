package com.zhevlakov.findjobtelegrambot.callback.handler;

import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.callback.CallbackContent;
import com.zhevlakov.findjobtelegrambot.callback.CallbackHandler;
import com.zhevlakov.findjobtelegrambot.callback.code.WorkFormatCode;
import com.zhevlakov.findjobtelegrambot.fsm.FsmStateCode;
import com.zhevlakov.findjobtelegrambot.fsm.QueryFsmWizardService;
import org.springframework.stereotype.Component;

@Component
public class NextCallbackHandler implements CallbackHandler {
    private final QueryFsmWizardService wizardService;

    public NextCallbackHandler(QueryFsmWizardService wizardService) {
        this.wizardService = wizardService;
    }

    @Override
    public BotResponse handle(CallbackContent callbackContent) {
        var chatId = callbackContent.chatId();
        return wizardService.processChoice(chatId, WorkFormatCode.IN_PERSON.getButtonText());
    }

    @Override
    public FsmStateCode queryCode() {
        return FsmStateCode.NEXT;
    }
}
