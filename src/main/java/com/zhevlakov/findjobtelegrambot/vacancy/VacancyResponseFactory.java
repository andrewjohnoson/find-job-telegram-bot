package com.zhevlakov.findjobtelegrambot.vacancy;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardButtonContent;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardGenerator;
import com.zhevlakov.findjobtelegrambot.user.vacancy.UserVacancy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
public class VacancyResponseFactory {
    private final KeyboardGenerator keyboardGenerator;

    public VacancyResponseFactory(
            KeyboardGenerator keyboardGenerator
    ) {
        this.keyboardGenerator = keyboardGenerator;
    }

    public List<BotResponse> buildResponse(
            Long userId,
            List<UserVacancy> vacancies,
            Function<UserVacancy, List<KeyboardButtonContent>> actionButtonsProvider
    ) {
        return vacancies.stream()
                .map(vacancy -> {
                    var vacancyId = vacancy.getVacancy().getId();
                    var url = vacancy.getVacancy().getUrl();

                    KeyboardButtonContent urlButton =  KeyboardButtonContent.urlButton(
                            InlineDataCode.VACANCY_CLICK.buttonText(),
                            InlineDataCode.VACANCY_CLICK.inlineButtonCode() + "-" + vacancyId,
                            url);

                    List<KeyboardButtonContent> buttons = new ArrayList<>();
                    buttons.add(urlButton);
                    buttons.addAll(actionButtonsProvider.apply(vacancy));

                    Keyboard keyboard = keyboardGenerator.buildInlineKeyboard(buttons);

                    return BotResponse.post(userId, vacancy.toString(), keyboard);
                })
                .toList();
    }
}
