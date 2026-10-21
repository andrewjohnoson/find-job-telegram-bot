package com.zhevlakov.findjobtelegrambot.vacancy.ui;

import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardButtonContent;
import com.zhevlakov.findjobtelegrambot.user.vacancy.UserVacancy;
import com.zhevlakov.findjobtelegrambot.user.vacancy.UserVacancyService;
import com.zhevlakov.findjobtelegrambot.vacancy.VacancySearchFilter;
import com.zhevlakov.findjobtelegrambot.vacancy.VacancyStatus;
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

    public List<BotResponse> outputFavouriteVacancies(Long userId, VacancySearchFilter filter) {
        List<UserVacancy> userVacancyList =
                userVacancyService.getUserVacanciesByStatus(filter, userId, VacancyStatus.FAVOURITE);

        log.info("Выводимые вакансии: {}", userVacancyList);

        List<UserVacancy> vacancies =
                userVacancyService.getUserVacanciesByStatus(filter, userId, VacancyStatus.FAVOURITE);

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
