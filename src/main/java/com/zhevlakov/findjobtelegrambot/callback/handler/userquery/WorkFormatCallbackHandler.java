package com.zhevlakov.findjobtelegrambot.callback.handler.userquery;

import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.callback.CallbackContent;
import com.zhevlakov.findjobtelegrambot.callback.CallbackHandler;
import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;
import com.zhevlakov.findjobtelegrambot.fsm.QueryFsmWizardService;
import com.zhevlakov.findjobtelegrambot.vacancy.query.converter.WorkFormat;
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

        try {
            WorkFormat.valueOf(type);
            return BotResponse.asList(wizardService.processChoice(chatId, type));
        } catch (IllegalArgumentException e) {
            log.error("Неизвестный callback-код для формата работы={}", e.toString());
            return BotResponse.asList(BotResponse.error(chatId, "Произошла ошибка или кнопка устарела."));
        }
    }

    @Override
    public InlineDataCode queryCode() {
        return InlineDataCode.ASK_WORK_FORMAT;
    }
}
