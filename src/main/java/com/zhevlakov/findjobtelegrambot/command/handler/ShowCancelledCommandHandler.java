package com.zhevlakov.findjobtelegrambot.command.handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;
import com.zhevlakov.findjobtelegrambot.command.CommandHandler;
import com.zhevlakov.findjobtelegrambot.command.CommandHandlerName;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardButtonContent;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardGenerator;
import com.zhevlakov.findjobtelegrambot.keyboard.provider.VacancyKeyboardProvider;
import com.zhevlakov.findjobtelegrambot.user.vacancy.UserVacancy;
import com.zhevlakov.findjobtelegrambot.user.vacancy.UserVacancyService;
import com.zhevlakov.findjobtelegrambot.vacancy.VacancySearchFilter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShowCancelledCommandHandler implements CommandHandler {
    private final UserVacancyService userVacancyService;
    private final VacancyKeyboardProvider keyboardProvider;
    private final KeyboardGenerator keyboardGenerator;

    public ShowCancelledCommandHandler(
            UserVacancyService userVacancyService,
            VacancyKeyboardProvider keyboardProvider,
            KeyboardGenerator keyboardGenerator
    ) {
        this.userVacancyService = userVacancyService;
        this.keyboardProvider = keyboardProvider;
        this.keyboardGenerator = keyboardGenerator;
    }

    @Override
    public List<BotResponse> handle(Message message) {
        VacancySearchFilter filter = new VacancySearchFilter(
                null,
                null
        );
        var userId = message.chat().id();

        List<UserVacancy> vacancies = userVacancyService.getUserVacanciesByStatus(filter, userId);

        return vacancies.stream()
                .map(vacancy -> {
                    var vacancyId = vacancy.getVacancy().getId();
                    var url = vacancy.getVacancy().getUrl();

                    List<KeyboardButtonContent> buttonList = List.of(
                            KeyboardButtonContent.urlButton(
                                    InlineDataCode.VACANCY_CLICK.buttonText(),
                                    InlineDataCode.VACANCY_CLICK.inlineButtonCode() + "_" + vacancyId,
                                    url),
                            KeyboardButtonContent.standardButton(
                                    InlineDataCode.VACANCY_UNCOVER.buttonText(),
                                    InlineDataCode.VACANCY_UNCOVER.inlineButtonCode() + "_" + vacancyId)
                    );

                    keyboardProvider.setButtonList(buttonList);
                    Keyboard keyboard = keyboardGenerator.buildInlineKeyboard(keyboardProvider, userId);

                    return BotResponse.post(userId, vacancy.getVacancy().toString(), keyboard);
                })
                .toList();
    }

    @Override
    public CommandHandlerName getCommandHandlerName() {
        return CommandHandlerName.SHOW_CANCELLED;
    }
}
