package com.zhevlakov.findjobtelegrambot.callback.handler.userquery;

import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.callback.CallbackContent;
import com.zhevlakov.findjobtelegrambot.callback.CallbackHandler;
import com.zhevlakov.findjobtelegrambot.callback.code.userquery.EmploymentTypeCode;
import com.zhevlakov.findjobtelegrambot.callback.code.userquery.QueryCode;
import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;
import com.zhevlakov.findjobtelegrambot.fsm.QueryFsmWizardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public List<BotResponse> handle(CallbackContent callbackContent) {
        var type = callbackContent.code();
        var chatId = callbackContent.chatId();

        if (type.equals(EmploymentTypeCode.FULL.getEmplCode())) {
            return BotResponse.asList(handleFull(chatId));
        }

        if (type.equals(EmploymentTypeCode.PART.getEmplCode())) {
            return BotResponse.asList(handlePart(chatId));
        }

        if (type.equals(EmploymentTypeCode.TRAINEE.getEmplCode())) {
            return BotResponse.asList(handleTrainee(chatId));
        }

        if (type.equals(QueryCode.NEXT.getExpCode())) {
            return BotResponse.asList(handleNext(chatId));
        }

        log.error("Не удалось обработать callback, chatId={}", chatId);
        var errorResponse = BotResponse.error(chatId, "Произошла ошибка.");
        return BotResponse.asList(errorResponse);
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
    public InlineDataCode queryCode() {
        return InlineDataCode.ASK_EMPLOYMENT;
    }
}
