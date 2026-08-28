package com.zhevlakov.findjobtelegrambot.callback.handler.userquery;

import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.callback.CallbackContent;
import com.zhevlakov.findjobtelegrambot.callback.CallbackHandler;
import com.zhevlakov.findjobtelegrambot.callback.code.userquery.QueryCode;
import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;
import com.zhevlakov.findjobtelegrambot.callback.code.userquery.ExperienceCode;
import com.zhevlakov.findjobtelegrambot.fsm.QueryFsmWizardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public List<BotResponse> handle(CallbackContent callbackContent) {
        var type = callbackContent.code();
        var chatId = callbackContent.chatId();

        if (type.equals(ExperienceCode.NO_EXP.getExpCode())) {
            return BotResponse.asList(handleNoExp(chatId));
        }

        if (type.equals(ExperienceCode.ONE_TO_THREE.getExpCode())) {
            return BotResponse.asList(handleOneThree(chatId));
        }

        if (type.equals(ExperienceCode.THREE_TO_SIX.getExpCode())) {
            return BotResponse.asList(handleThreeSix(chatId));
        }

        if (type.equals(ExperienceCode.SIX_AND_MORE.getExpCode())) {
            return BotResponse.asList(handleSixMore(chatId));
        }

        if (type.equals(QueryCode.NEXT.getExpCode())) {
            return BotResponse.asList(handleNext(chatId));
        }

        log.error("Не удалось обработать callback, chatId={}", chatId);
        var errorResponse = BotResponse.error(chatId, "Произошла ошибка.");
        return BotResponse.asList(errorResponse);
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
    public InlineDataCode queryCode() {
        return InlineDataCode.ASK_EXPERIENCE;
    }
}
