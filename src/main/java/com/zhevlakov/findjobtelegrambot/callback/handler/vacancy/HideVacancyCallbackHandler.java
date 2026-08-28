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

import java.util.List;

@Component
public class HideVacancyCallbackHandler implements CallbackHandler {
    private final UserVacancyService userVacancyService;
    private final Logger log = LoggerFactory.getLogger(HideVacancyCallbackHandler.class);

    public HideVacancyCallbackHandler(UserVacancyService userVacancyService) {
        this.userVacancyService = userVacancyService;
    }

    @Override
    public List<BotResponse> handle(CallbackContent callbackContent) {
        var chatId = callbackContent.chatId();
        var vacancyId = callbackContent.additionalId();

        userVacancyService.changeVacancyStatus(vacancyId, VacancyStatus.HIDDEN);

        log.info("Вакансия vacancyId = {} спрятана для пользователя chatId = {}", vacancyId, chatId);
        var response = BotResponse.post(chatId, "Пост был скрыт.", null, false, false, true);
        return BotResponse.asList(response);
    }

    @Override
    public InlineDataCode queryCode() {
        return InlineDataCode.VACANCY_HIDE;
    }
}
