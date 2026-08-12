package com.zhevlakov.findjobtelegrambot.callback.handler;

import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.callback.CallbackContent;
import com.zhevlakov.findjobtelegrambot.callback.CallbackHandler;
import com.zhevlakov.findjobtelegrambot.callback.code.QueryCode;
import com.zhevlakov.findjobtelegrambot.fsm.FsmStateCode;
import com.zhevlakov.findjobtelegrambot.callback.code.ExperienceCode;
import com.zhevlakov.findjobtelegrambot.fsm.QueryFsmWizardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ExperienceCallbackHandler implements CallbackHandler {
    private final Logger log = LoggerFactory.getLogger(ExperienceCallbackHandler.class);
    private final QueryFsmWizardService wizardService;

    public ExperienceCallbackHandler(
            QueryFsmWizardService wizardService
    ) {
        this.wizardService = wizardService;
    }

    @Override
    public BotResponse handle(CallbackContent callbackContent) {
        var type = callbackContent.code();
        var chatId = callbackContent.chatId();

        if (type.equals(ExperienceCode.NO_EXP.getExpCode())) {
            return handleNoExp(chatId);
        }

        if (type.equals(ExperienceCode.ONE_TO_THREE.getExpCode())) {
            return handleOneThree(chatId);
        }

        if (type.equals(ExperienceCode.THREE_TO_SIX.getExpCode())) {
            return handleThreeSix(chatId);
        }

        if (type.equals(ExperienceCode.SIX_AND_MORE.getExpCode())) {
            return handleSixMore(chatId);
        }

        if (type.equals(QueryCode.NEXT.getExpCode())) {
            return handleNext(chatId);
        }

        log.error("Не удалось обработать callback, chatId={}", chatId);
        return BotResponse.error(chatId, "Произошла ошибка.");
    }

    private BotResponse handleNoExp(Long chatId) {
        return wizardService.processChoice(chatId, ExperienceCode.NO_EXP.getButtonText());
    }

    private BotResponse handleOneThree(Long chatId) {
        return wizardService.processChoice(chatId, ExperienceCode.ONE_TO_THREE.getButtonText());
    }

    private BotResponse handleThreeSix(Long chatId) {
        return wizardService.processChoice(chatId, ExperienceCode.THREE_TO_SIX.getButtonText());
    }

    private BotResponse handleSixMore(Long chatId) {
        return wizardService.processChoice(chatId, ExperienceCode.SIX_AND_MORE.getButtonText());
    }

    private BotResponse handleNext(Long chatId) {
        return wizardService.processChoice(chatId, null);
    }

    @Override
    public FsmStateCode queryCode() {
        return FsmStateCode.ASK_EXPERIENCE;
    }
}
