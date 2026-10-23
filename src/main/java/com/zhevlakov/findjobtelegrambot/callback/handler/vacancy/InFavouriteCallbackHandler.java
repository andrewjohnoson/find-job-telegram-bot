package com.zhevlakov.findjobtelegrambot.callback.handler.vacancy;

import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.callback.CallbackContent;
import com.zhevlakov.findjobtelegrambot.callback.CallbackHandler;
import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;
import com.zhevlakov.findjobtelegrambot.user.vacancy.UserVacancyService;
import com.zhevlakov.findjobtelegrambot.vacancy.VacancyStatus;
import jakarta.transaction.Transactional;
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

    @Transactional
    @Override
    public List<BotResponse> handle(CallbackContent callbackContent) {
        var userId = callbackContent.chatId();
        var vacancyId = callbackContent.additionalId();

        if (userVacancyService.hasStatus(userId, vacancyId, VacancyStatus.FAVOURITE)) {
            log.warn("Пост в чате {} с вакансией {} уже в избранном", userId, vacancyId);
            var errorResponse = BotResponse.error(userId, "Пост уже в избранном.");
            return BotResponse.asList(errorResponse);
        }

        userVacancyService.changeVacancyStatus(userId, vacancyId, VacancyStatus.FAVOURITE);
        userVacancyService.makeVisible(userId, vacancyId);

        log.info("Вакансия vacancyId = {} добавлено в избарнное пользователя userId = {}", vacancyId, userId);
        var response = BotResponse.post(userId, "Пост добавлен в избранное.", null, false, false, true);
        return BotResponse.asList(response);
    }

    @Override
    public InlineDataCode queryCode() {
        return InlineDataCode.VACANCY_FAVOURITE;
    }
}
