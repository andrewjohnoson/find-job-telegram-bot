package com.zhevlakov.findjobtelegrambot.vacancy.provider;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.bot.BotResponseMapper;
import com.zhevlakov.findjobtelegrambot.bot.TelegramMessageSenderService;
import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardButtonContent;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardGenerator;
import com.zhevlakov.findjobtelegrambot.keyboard.provider.VacancyKeyboardProvider;
import com.zhevlakov.findjobtelegrambot.user.vacancy.UserVacancy;
import com.zhevlakov.findjobtelegrambot.user.vacancy.UserVacancyService;
import com.zhevlakov.findjobtelegrambot.vacancy.VacancySearchFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestProvider {
    private final UserVacancyService userVacancyService;
    private final VacancyKeyboardProvider keyboardProvider;
    private final KeyboardGenerator keyboardGenerator;
    private final Logger log = LoggerFactory.getLogger(TestProvider.class);
    private final TelegramMessageSenderService telegramMessageSenderService;
    private final BotResponseMapper botResponseMapper;

    public TestProvider(
            UserVacancyService userVacancyService,
            VacancyKeyboardProvider keyboardProvider,
            KeyboardGenerator keyboardGenerator,
            TelegramMessageSenderService telegramMessageSenderService,
            BotResponseMapper botResponseMapper
    ) {
        this.userVacancyService = userVacancyService;
        this.keyboardProvider = keyboardProvider;
        this.keyboardGenerator = keyboardGenerator;
        this.telegramMessageSenderService = telegramMessageSenderService;
        this.botResponseMapper = botResponseMapper;
    }

    public void fetchVacancies(Long userId) {
        VacancySearchFilter filter = new VacancySearchFilter(
                null,
                null
        );

        List<UserVacancy> vacancies = userVacancyService.getVisibleUserVacanciesByFilter(filter, userId);

        List<BotResponse> botResponseList = vacancies.stream()
                .map(vacancy -> {
                    var vacancyId = vacancy.getVacancy().getId();
                    var url = vacancy.getVacancy().getUrl();

                    List<KeyboardButtonContent> buttonList = List.of(
                            KeyboardButtonContent.urlButton(InlineDataCode.VACANCY_CLICK.buttonText(),
                                    InlineDataCode.VACANCY_CLICK.inlineButtonCode() + "_" + vacancyId,
                                    url),
                            KeyboardButtonContent.standardButton(InlineDataCode.VACANCY_FAVOURITE.buttonText(),
                                    InlineDataCode.VACANCY_FAVOURITE.inlineButtonCode() + "_" + vacancyId),
                            KeyboardButtonContent.standardButton(InlineDataCode.VACANCY_HIDE.buttonText(),
                                    InlineDataCode.VACANCY_HIDE.inlineButtonCode() + "_" + vacancyId)
                    );

                    keyboardProvider.setButtonList(buttonList);
                    Keyboard keyboard = keyboardGenerator.buildInlineKeyboard(keyboardProvider, userId);

                    return BotResponse.post(userId, vacancy.getVacancy().toString(), keyboard);
                })
                .toList();

        var requestList = botResponseList.stream()
                        .map(botResponseMapper::toRequest)
                        .toList();

        telegramMessageSenderService.sendMessage(requestList);
    }
}