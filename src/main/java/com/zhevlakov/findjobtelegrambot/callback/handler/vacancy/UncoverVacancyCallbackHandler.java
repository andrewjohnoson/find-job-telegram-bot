package com.zhevlakov.findjobtelegrambot.callback.handler.vacancy;

import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.callback.CallbackContent;
import com.zhevlakov.findjobtelegrambot.callback.CallbackHandler;
import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;
import com.zhevlakov.findjobtelegrambot.user.vacancy.UserVacancyService;
import com.zhevlakov.findjobtelegrambot.vacancy.VacancyStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UncoverVacancyCallbackHandler implements CallbackHandler {
    private final UserVacancyService userVacancyService;
    private final Logger log = LoggerFactory.getLogger(UncoverVacancyCallbackHandler.class);

    public UncoverVacancyCallbackHandler(
            UserVacancyService userVacancyService
    ) {
        this.userVacancyService = userVacancyService;
    }

    @Override
    public BotResponse handle(CallbackContent callbackContent) {
        var chatId = callbackContent.chatId();
        var vacancyId = callbackContent.additionalId();

        userVacancyService.changeVacancyStatus(vacancyId, VacancyStatus.FREE);

        log.info("Вакансия vacancyId = {} показана для пользователя chatId = {}", vacancyId, chatId);
        return BotResponse.post(chatId, "Пост вновь показывается.", null, false, false, true);
    }

    @Override
    public InlineDataCode queryCode() {
        return InlineDataCode.VACANCY_UNCOVER;
    }
}
