package com.zhevlakov.findjobtelegrambot.callback.handler.userquery;

import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.callback.CallbackContent;
import com.zhevlakov.findjobtelegrambot.callback.CallbackHandler;
import com.zhevlakov.findjobtelegrambot.callback.code.userquery.QueryCode;
import com.zhevlakov.findjobtelegrambot.callback.code.userquery.WorkFormatCode;
import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;
import com.zhevlakov.findjobtelegrambot.fsm.QueryFsmWizardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public List<BotResponse> handle(CallbackContent callbackContent) {
        var type = callbackContent.code();
        var chatId = callbackContent.chatId();

        if (type.equals(WorkFormatCode.IN_PERSON.getExpCode())) {
            return BotResponse.asList(handleInPerson(chatId));
        }

        if (type.equals(WorkFormatCode.REMOTE.getExpCode())) {
            return BotResponse.asList(handleRemote(chatId));
        }

        if (type.equals(WorkFormatCode.HYBRID.getExpCode())) {
            return BotResponse.asList(handleHybrid(chatId));
        }

        if (type.equals(QueryCode.NEXT.getExpCode())) {
            return BotResponse.asList(handleNext(chatId));
        }

        log.error("Не удалось обработать callback, chatId={}", chatId);
        var errorResponse = BotResponse.error(chatId, "Произошла ошибка.");
        return BotResponse.asList(errorResponse);
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
    public InlineDataCode queryCode() {
        return InlineDataCode.ASK_WORK_FORMAT;
    }
}
