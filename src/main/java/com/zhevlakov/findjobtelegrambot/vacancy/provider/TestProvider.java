package com.zhevlakov.findjobtelegrambot.vacancy.provider;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.bot.BotResponseMapper;
import com.zhevlakov.findjobtelegrambot.bot.TelegramMessageSenderService;
import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardButtonContent;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardGenerator;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardProvider;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardSettings;
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
    private final TestKeyboardProvider keyboardProvider;
    private final KeyboardGenerator keyboardGenerator;
    private final Logger log = LoggerFactory.getLogger(TestProvider.class);
    private final TelegramMessageSenderService telegramMessageSenderService;
    private final BotResponseMapper botResponseMapper;

    public TestProvider(
            UserVacancyService userVacancyService,
            TestKeyboardProvider keyboardProvider,
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

        List<UserVacancy> vacancies = userVacancyService.getUserVacanciesByFilter(filter, userId);

        List<BotResponse> botResponseList = vacancies.stream()
                .map(vacancy -> {
                    var vacancyId = vacancy.getVacancy().getId();
                    var url = vacancy.getVacancy().getUrl();

                    List<KeyboardButtonContent> buttonList = List.of(
                            KeyboardButtonContent.urlButton("Перейти",
                                    InlineDataCode.VACANCY_CLICK.getInlineButtonCode() + "_" + vacancyId,
                                    url),
                            KeyboardButtonContent.standardButton("В избранное",
                                    InlineDataCode.VACANCY_FAVOURITE.getInlineButtonCode() + "_" + vacancyId),
                            KeyboardButtonContent.standardButton("Не показывать",
                                    InlineDataCode.VACANCY_HIDE.getInlineButtonCode() + "_" + vacancyId)
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

@Component
class TestKeyboardProvider implements KeyboardProvider {
    private List<KeyboardButtonContent> buttonList;

    public void setButtonList(List<KeyboardButtonContent> buttonList) {
        this.buttonList = buttonList;
    }

    @Override
    public KeyboardSettings getKeyboardSettings(Long chatId) {

        return new KeyboardSettings(
                buttonList,
                null,
                InlineDataCode.VACANCY.getInlineButtonCode()
        );
    }
}