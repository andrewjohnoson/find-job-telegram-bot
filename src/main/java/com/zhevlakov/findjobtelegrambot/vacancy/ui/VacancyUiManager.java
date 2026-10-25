package com.zhevlakov.findjobtelegrambot.vacancy.ui;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardButtonContent;
import com.zhevlakov.findjobtelegrambot.user.vacancy.UserVacancy;
import com.zhevlakov.findjobtelegrambot.user.vacancy.UserVacancyService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VacancyUiManager {
    private final VacancyResponseFactory responseFactory;
    private final UserVacancyService userVacancyService;
    private final Logger log = LoggerFactory.getLogger(VacancyUiManager.class);

    public VacancyUiManager(
            VacancyResponseFactory responseFactory,
            UserVacancyService userVacancyService
    ) {
        this.responseFactory = responseFactory;
        this.userVacancyService = userVacancyService;
    }

    @Transactional
    public List<BotResponse> outputVacancies(Long userId) {
        List<UserVacancy> userVacancyList = userVacancyService.getNewUserVacancies(userId);
        log.info("Выводимые вакансии: {}", userVacancyList);

        // Проверка: есть есть ли в списке вакансии
        if (userVacancyList == null || userVacancyList.isEmpty()) {
            log.info("Все вакансии уже показаны для пользователя id={}.", userId);
            return BotResponse.asList(BotResponse.post(userId, "Все вакансии уже показаны."));
        }

        // Добавляются кнопки "В избранное" и "Скрыть"
        return responseFactory.buildResponse(userId, userVacancyList,
                vacancy -> {
                    var vacancyId = vacancy.getVacancy().getId();
                    return List.of(
                            KeyboardButtonContent.standardButton(
                                    InlineDataCode.VACANCY_FAVOURITE.buttonText(),
                                    InlineDataCode.VACANCY_FAVOURITE.inlineButtonCode() + "-" + vacancyId),
                            KeyboardButtonContent.standardButton(
                                    InlineDataCode.VACANCY_HIDE.buttonText(),
                                    InlineDataCode.VACANCY_HIDE.inlineButtonCode() + "-" + vacancyId)
                    );
                });
    }

    @Transactional
    public List<BotResponse> outputFavouriteVacancies(Long userId) {
        List<UserVacancy> vacancies = userVacancyService.getFavouriteUserVacancies(userId);
        log.info("Выводимые избранные вакансии: {}", vacancies);

        // Проверка: есть ли избранные вакансии
        if (vacancies == null || vacancies.isEmpty()) {
            log.info("Нет избранных вакансий у пользователя id={}", userId);

            Keyboard keyboard = responseFactory.generateContinueVacancySearchingKeyboard();
            return BotResponse.asList(BotResponse.post(userId, "У вас нет избранных вакансий.", keyboard));
        }

        // Генерируются вакансии с кнопкой "Убрать из избранных"
        return responseFactory.buildResponse(
                userId,
                vacancies,
                vacancy -> {
                    var vacancyId = vacancy.getVacancy().getId();
                    return List.of(
                            KeyboardButtonContent.standardButton(
                                    InlineDataCode.VACANCY_NOT_FAVOURITE.buttonText(),
                                    InlineDataCode.VACANCY_NOT_FAVOURITE.inlineButtonCode() + "-" + vacancyId)
                    );
                }
        );
    }
}
