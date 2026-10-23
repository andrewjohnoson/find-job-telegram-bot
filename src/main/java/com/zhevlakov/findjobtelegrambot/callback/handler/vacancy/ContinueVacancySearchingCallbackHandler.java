package com.zhevlakov.findjobtelegrambot.callback.handler.vacancy;

import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.callback.CallbackContent;
import com.zhevlakov.findjobtelegrambot.callback.CallbackHandler;
import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;
import com.zhevlakov.findjobtelegrambot.fsm.FsmStates;
import com.zhevlakov.findjobtelegrambot.user.UserService;
import com.zhevlakov.findjobtelegrambot.vacancy.ui.VacancyUiManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ContinueVacancySearchingCallbackHandler implements CallbackHandler {
    private final Logger log = LoggerFactory.getLogger(ContinueVacancySearchingCallbackHandler.class);
    private final UserService userService;
    private final VacancyUiManager vacancyUiManager;

    public ContinueVacancySearchingCallbackHandler(
            UserService userService,
            VacancyUiManager vacancyUiManager
    ) {
        this.userService = userService;
        this.vacancyUiManager = vacancyUiManager;
    }

    @Override
    public List<BotResponse> handle(CallbackContent callbackContent) {
        var userId = callbackContent.chatId();

        userService.changeUserState(userId, FsmStates.FREE);
        log.info("Пользователь id={} перестал смотреть избранные вакансии.", userId);

        BotResponse deleteKeyboard = BotResponse.post(userId, "Продолжается поиск.", null, true);
        List<BotResponse> responses = new ArrayList<>(vacancyUiManager.outputVacancies(userId));
        responses.addFirst(deleteKeyboard);
        return responses;
    }

    @Override
    public InlineDataCode queryCode() {
        return InlineDataCode.CONTINUE_VACANCY_SEARCHING;
    }
}
