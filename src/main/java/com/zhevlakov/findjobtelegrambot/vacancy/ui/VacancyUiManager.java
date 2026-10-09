package com.zhevlakov.findjobtelegrambot.vacancy.ui;

import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardButtonContent;
import com.zhevlakov.findjobtelegrambot.user.UserEntity;
import com.zhevlakov.findjobtelegrambot.user.UserService;
import com.zhevlakov.findjobtelegrambot.user.vacancy.UserVacancy;
import com.zhevlakov.findjobtelegrambot.user.vacancy.UserVacancyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VacancyUiManager {
    private final VacancyResponseFactory responseFactory;
    private final UserService userService;
    private final UserVacancyService userVacancyService;
    private final Logger log = LoggerFactory.getLogger(VacancyUiManager.class);


    public VacancyUiManager(
            VacancyResponseFactory responseFactory,
            UserService userService,
            UserVacancyService userVacancyService
    ) {
        this.responseFactory = responseFactory;
        this.userService = userService;
        this.userVacancyService = userVacancyService;
    }

    public List<BotResponse> outputVacancies(Long userId) {
        List<UserVacancy> userVacancyList = userVacancyService.getNewUserVacancies(userId);

        log.info("Выводимые вакансии {}", userVacancyList);
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
}
