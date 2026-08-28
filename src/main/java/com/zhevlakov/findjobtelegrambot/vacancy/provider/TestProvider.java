package com.zhevlakov.findjobtelegrambot.vacancy.provider;

import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.bot.BotResponseMapper;
import com.zhevlakov.findjobtelegrambot.bot.TelegramMessageSenderService;
import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardButtonContent;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardGenerator;
import com.zhevlakov.findjobtelegrambot.user.vacancy.UserVacancy;
import com.zhevlakov.findjobtelegrambot.user.vacancy.UserVacancyService;
import com.zhevlakov.findjobtelegrambot.vacancy.VacancyResponseFactory;
import com.zhevlakov.findjobtelegrambot.vacancy.VacancySearchFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TestProvider {
    private final static int PAGE_SIZE = 5;

    private final UserVacancyService userVacancyService;
    private final Logger log = LoggerFactory.getLogger(TestProvider.class);
    private final TelegramMessageSenderService telegramMessageSenderService;
    private final BotResponseMapper botResponseMapper;
    private final VacancyResponseFactory vacancyResponseFactory;
    private final KeyboardGenerator keyboardGenerator;

    public TestProvider(
            UserVacancyService userVacancyService,
            TelegramMessageSenderService telegramMessageSenderService,
            BotResponseMapper botResponseMapper,
            VacancyResponseFactory vacancyResponseFactory,
            KeyboardGenerator keyboardGenerator
    ) {
        this.userVacancyService = userVacancyService;
        this.telegramMessageSenderService = telegramMessageSenderService;
        this.botResponseMapper = botResponseMapper;
        this.vacancyResponseFactory = vacancyResponseFactory;
        this.keyboardGenerator = keyboardGenerator;
    }

    public List<BotResponse> fetchVacancies(Long userId) {
        return fetchVacancies(userId, 0);
    }

    public List<BotResponse> fetchVacancies(Long userId, Integer pageNum) {
        VacancySearchFilter filter = new VacancySearchFilter(
                PAGE_SIZE,
                pageNum
        );

        List<UserVacancy> vacancies = userVacancyService.getVisibleUserVacanciesByFilter(filter, userId);

        if (vacancies.isEmpty()) {
            String text = pageNum == 0 ?
                    "По вашему запросу новых вакансий не найдено." :
                    "Вы просмотрели все доступные вакансии! Больше пока что нет";

            var emptyResponse = BotResponse.error(userId, text);
            telegramMessageSenderService.sendMessage(botResponseMapper.toRequest(emptyResponse));
        }

        List<BotResponse> botResponseList = new ArrayList<>(vacancyResponseFactory.buildResponse(
                userId,
                vacancies,
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
                }
        ));

        if (vacancies.size() == PAGE_SIZE) {
            int nextPage = pageNum + 1;

            List<KeyboardButtonContent> moreButton = List.of(
                    KeyboardButtonContent.standardButton(
                            InlineDataCode.VACANCY_LOAD_MORE.buttonText(),
                            InlineDataCode.VACANCY_LOAD_MORE.inlineButtonCode() + "-" + nextPage)
            );

            var keyboard = keyboardGenerator.buildInlineKeyboard(moreButton);

            String message = "Показано " + PAGE_SIZE + " вакансий. Загрузить следующие?";
            botResponseList.add(BotResponse.post(userId, message, keyboard, true));
        }

        return botResponseList;

        /*
        var requestList = botResponseList.stream()
                        .map(botResponseMapper::toRequest)
                        .toList();

        telegramMessageSenderService.sendMessage(requestList); */
    }
}