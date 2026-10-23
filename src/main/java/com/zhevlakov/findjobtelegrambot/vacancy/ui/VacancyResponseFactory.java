package com.zhevlakov.findjobtelegrambot.vacancy.ui;

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
import java.util.stream.Collectors;

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
        List<BotResponse> responses = vacancies.stream()
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
                .collect(Collectors.toCollection(ArrayList::new));

        Keyboard showMoreKeyboard = generateLoadMoreKeyboard();

        responses.add(BotResponse.post(userId, "Нажмите, чтобы показать дальше.", showMoreKeyboard));
        return responses;
    }

    private Keyboard generateLoadMoreKeyboard() {
        KeyboardButtonContent showMoreButton = KeyboardButtonContent.standardButton(
                InlineDataCode.VACANCY_LOAD_MORE.buttonText(),
                InlineDataCode.VACANCY_LOAD_MORE.inlineButtonCode());

        return keyboardGenerator.buildInlineKeyboard(List.of(showMoreButton));
    }

    public Keyboard generateContinueVacancySearchingKeyboard() {
        List<KeyboardButtonContent> button = List.of(
                KeyboardButtonContent.standardButton(
                        InlineDataCode.CONTINUE_VACANCY_SEARCHING.buttonText(),
                        InlineDataCode.CONTINUE_VACANCY_SEARCHING.inlineButtonCode()
                )
        );

        return keyboardGenerator.buildInlineKeyboard(button);
    }
}
