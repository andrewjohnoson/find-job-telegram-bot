package com.zhevlakov.findjobtelegrambot.callback.handler;

import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.callback.CallbackContent;
import com.zhevlakov.findjobtelegrambot.callback.CallbackHandler;
import com.zhevlakov.findjobtelegrambot.callback.code.QueryCode;
import com.zhevlakov.findjobtelegrambot.callback.code.WorkFormatCode;
import com.zhevlakov.findjobtelegrambot.fsm.FsmStateCode;
import com.zhevlakov.findjobtelegrambot.fsm.QueryFsmWizardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WorkFormatCallbackHandler implements CallbackHandler {
    private final QueryFsmWizardService wizardService;
    private final Logger log = LoggerFactory.getLogger(WorkFormatCallbackHandler.class);

    public WorkFormatCallbackHandler(
            QueryFsmWizardService wizardService
    ) {
        this.wizardService = wizardService;
    }

    @Override
    public BotResponse handle(CallbackContent callbackContent) {
        var type = callbackContent.code();
        var chatId = callbackContent.chatId();

        if (type.equals(WorkFormatCode.IN_PERSON.getExpCode())) {
            return handleInPerson(chatId);
        }

        if (type.equals(WorkFormatCode.REMOTE.getExpCode())) {
            return handleRemote(chatId);
        }

        if (type.equals(WorkFormatCode.HYBRID.getExpCode())) {
            return handleHybrid(chatId);
        }

        if (type.equals(QueryCode.NEXT.getExpCode())) {
            return handleNext(chatId);
        }

        log.error("Не удалось обработать callback, chatId={}", chatId);
        return BotResponse.error(chatId, "Произошла ошибка.");
    }

    private BotResponse handleInPerson(Long chatId) {
        return wizardService.processChoice(chatId, WorkFormatCode.IN_PERSON.getButtonText());
    }

    private BotResponse handleRemote(Long chatId) {
        return wizardService.processChoice(chatId, WorkFormatCode.REMOTE.getButtonText());
    }

    private BotResponse handleHybrid(Long chatId) {
        return wizardService.processChoice(chatId, WorkFormatCode.HYBRID.getButtonText());
    }

    private BotResponse handleNext(Long chatId) {
        return wizardService.processChoice(chatId, null);
    }

    @Override
    public FsmStateCode queryCode() {
        return FsmStateCode.ASK_WORK_FORMAT;
    }
}
