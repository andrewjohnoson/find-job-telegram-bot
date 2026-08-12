package com.zhevlakov.findjobtelegrambot.callback.handler;

import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.callback.CallbackContent;
import com.zhevlakov.findjobtelegrambot.callback.CallbackHandler;
import com.zhevlakov.findjobtelegrambot.callback.code.EmploymentTypeCode;
import com.zhevlakov.findjobtelegrambot.callback.code.QueryCode;
import com.zhevlakov.findjobtelegrambot.fsm.FsmStateCode;
import com.zhevlakov.findjobtelegrambot.fsm.QueryFsmWizardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EmploymentCallbackHandler implements CallbackHandler {
    private final QueryFsmWizardService wizardService;
    private final Logger log = LoggerFactory.getLogger(EmploymentCallbackHandler.class);

    public EmploymentCallbackHandler(
            QueryFsmWizardService wizardService
    ) {
        this.wizardService = wizardService;
    }

    @Override
    public BotResponse handle(CallbackContent callbackContent) {
        var type = callbackContent.code();
        var chatId = callbackContent.chatId();

        if (type.equals(EmploymentTypeCode.FULL.getExpCode())) {
            return handleFull(chatId);
        }

        if (type.equals(EmploymentTypeCode.PART.getExpCode())) {
            return handlePart(chatId);
        }

        if (type.equals(EmploymentTypeCode.TRAINEE.getExpCode())) {
            return handleTrainee(chatId);
        }

        if (type.equals(QueryCode.NEXT.getExpCode())) {
            return handleNext(chatId);
        }

        log.error("Не удалось обработать callback, chatId={}", chatId);
        return BotResponse.error(chatId, "Произошла ошибка.");
    }

    private BotResponse handleFull(Long chatId) {
        return wizardService.processChoice(chatId, EmploymentTypeCode.FULL.getButtonText());
    }

    private BotResponse handlePart(Long chatId) {
        return wizardService.processChoice(chatId, EmploymentTypeCode.PART.getButtonText());
    }

    private BotResponse handleTrainee(Long chatId) {
        return wizardService.processChoice(chatId, EmploymentTypeCode.TRAINEE.getButtonText());
    }

    private BotResponse handleNext(Long chatId) {
        return wizardService.processChoice(chatId, null);
    }

    @Override
    public FsmStateCode queryCode() {
        return FsmStateCode.ASK_EMPLOYMENT;
    }
}
