package com.zhevlakov.findjobtelegrambot.callback.handler.vacancy;

import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.callback.CallbackContent;
import com.zhevlakov.findjobtelegrambot.callback.CallbackHandler;
import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;
import com.zhevlakov.findjobtelegrambot.user.vacancy.UserVacancyService;
import com.zhevlakov.findjobtelegrambot.vacancy.VacancyStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InFavouriteCallbackHandler implements CallbackHandler {
    private final UserVacancyService userVacancyService;
    private final Logger log = LoggerFactory.getLogger(InFavouriteCallbackHandler.class);

    @Autowired
    public InFavouriteCallbackHandler(
            UserVacancyService userVacancyService
    ) {
        this.userVacancyService = userVacancyService;
    }

    @Override
    public List<BotResponse> handle(CallbackContent callbackContent) {
        var chatId = callbackContent.chatId();
        var vacancyId = callbackContent.additionalId();

        if (userVacancyService.hasStatus(vacancyId, VacancyStatus.FAVOURITE)) {
            var errorResponse = BotResponse.error(chatId, "Пост уже в избранном.");
            return BotResponse.asList(errorResponse);
        }

        userVacancyService.changeVacancyStatus(vacancyId, VacancyStatus.FAVOURITE);

        log.info("Вакансия vacancyId = {} добавлено в избарнное пользователя chatId = {}", vacancyId, chatId);
        var response = BotResponse.post(chatId, "Пост добавлен в избранное.", null, false, false, true);
        return BotResponse.asList(response);
    }

    @Override
    public InlineDataCode queryCode() {
        return InlineDataCode.VACANCY_FAVOURITE;
    }
}
