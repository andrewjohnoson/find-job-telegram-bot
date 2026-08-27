package com.zhevlakov.findjobtelegrambot.vacancy.provider;

import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.bot.BotResponseMapper;
import com.zhevlakov.findjobtelegrambot.bot.TelegramMessageSenderService;
import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardButtonContent;
import com.zhevlakov.findjobtelegrambot.user.vacancy.UserVacancy;
import com.zhevlakov.findjobtelegrambot.user.vacancy.UserVacancyService;
import com.zhevlakov.findjobtelegrambot.vacancy.VacancyResponseFactory;
import com.zhevlakov.findjobtelegrambot.vacancy.VacancySearchFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestProvider {
    private final UserVacancyService userVacancyService;
    private final Logger log = LoggerFactory.getLogger(TestProvider.class);
    private final TelegramMessageSenderService telegramMessageSenderService;
    private final BotResponseMapper botResponseMapper;
    private final VacancyResponseFactory vacancyResponseFactory;

    public TestProvider(
            UserVacancyService userVacancyService,
            TelegramMessageSenderService telegramMessageSenderService,
            BotResponseMapper botResponseMapper,
            VacancyResponseFactory vacancyResponseFactory
    ) {
        this.userVacancyService = userVacancyService;
        this.telegramMessageSenderService = telegramMessageSenderService;
        this.botResponseMapper = botResponseMapper;
        this.vacancyResponseFactory = vacancyResponseFactory;
    }

    public void fetchVacancies(Long userId) {
        VacancySearchFilter filter = new VacancySearchFilter(
                null,
                null
        );

        List<UserVacancy> vacancies = userVacancyService.getVisibleUserVacanciesByFilter(filter, userId);

        List<BotResponse> botResponseList = vacancyResponseFactory.buildResponse(
                userId,
                vacancies,
                vacancy -> {
                    var vacancyId = vacancy.getVacancy().getId();
                    return List.of(
                            KeyboardButtonContent.standardButton(
                                    InlineDataCode.VACANCY_FAVOURITE.buttonText(),
                                    InlineDataCode.VACANCY_FAVOURITE.inlineButtonCode() + "-" + vacancyId),
//                                    "-" + vacancyId),
                            KeyboardButtonContent.standardButton(InlineDataCode.VACANCY_HIDE.buttonText(),
                                    InlineDataCode.VACANCY_HIDE.inlineButtonCode() + "-" + vacancyId)
//                                    "-" + vacancyId)
                            );
                }
        );

        var requestList = botResponseList.stream()
                        .map(botResponseMapper::toRequest)
                        .toList();

        telegramMessageSenderService.sendMessage(requestList);
    }
}