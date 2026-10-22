package com.zhevlakov.findjobtelegrambot.callback.handler.vacancy;

import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.callback.CallbackContent;
import com.zhevlakov.findjobtelegrambot.callback.CallbackHandler;
import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;
import com.zhevlakov.findjobtelegrambot.fsm.FsmStates;
import com.zhevlakov.findjobtelegrambot.user.UserService;
import com.zhevlakov.findjobtelegrambot.vacancy.ui.VacancyUiManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LoadMoreCallbackHandler implements CallbackHandler {
    private final VacancyUiManager vacancyUiManager;
    private final UserService userService;

    public LoadMoreCallbackHandler(
            VacancyUiManager vacancyUiManager,
            UserService userService
    ) {
        this.vacancyUiManager = vacancyUiManager;
        this.userService = userService;
    }

    @Override
    public List<BotResponse> handle(CallbackContent callbackContent) {
        var userId = callbackContent.chatId();
        BotResponse deleteKeyboard = BotResponse.post(userId, "Загрузка", null, true);

        List<BotResponse> responses = new ArrayList<>();

        if (userService.hasState(userId, FsmStates.FREE)) {
            responses.addAll(vacancyUiManager.outputVacancies(userId));
        }

        if (userService.hasState(userId, FsmStates.WATCH_FAVOURITE)) {
            responses.addAll(vacancyUiManager.outputFavouriteVacancies(userId));
        }

        responses.addFirst(deleteKeyboard);

        return responses;
    }

    @Override
    public InlineDataCode queryCode() {
        return InlineDataCode.VACANCY_LOAD_MORE;
    }
}
