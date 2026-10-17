package com.zhevlakov.findjobtelegrambot.callback.handler.vacancy;

import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.callback.CallbackContent;
import com.zhevlakov.findjobtelegrambot.callback.CallbackHandler;
import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;
import com.zhevlakov.findjobtelegrambot.vacancy.ui.VacancyUiManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LoadMoreCallbackHandler implements CallbackHandler {
    private final VacancyUiManager vacancyUiManager;

    public LoadMoreCallbackHandler(
            VacancyUiManager vacancyUiManager
    ) {
        this.vacancyUiManager = vacancyUiManager;
    }

    @Override
    public List<BotResponse> handle(CallbackContent callbackContent) {
        var userId = callbackContent.chatId();
//        var page = Math.toIntExact(callbackContent.additionalId());
        BotResponse deleteKeyboard = BotResponse.post(userId, "Загрузка", null, true);

        List<BotResponse> responses = new ArrayList<>(vacancyUiManager.outputVacancies(userId/*, page*/));
        responses.addFirst(deleteKeyboard);

        return responses;
    }

    @Override
    public InlineDataCode queryCode() {
        return InlineDataCode.VACANCY_LOAD_MORE;
    }
}
