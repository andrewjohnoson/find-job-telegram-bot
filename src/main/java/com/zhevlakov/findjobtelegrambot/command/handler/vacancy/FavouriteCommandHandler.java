package com.zhevlakov.findjobtelegrambot.command.handler.vacancy;

import com.pengrad.telegrambot.model.Message;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;
import com.zhevlakov.findjobtelegrambot.command.CommandHandler;
import com.zhevlakov.findjobtelegrambot.command.CommandHandlerName;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardButtonContent;
import com.zhevlakov.findjobtelegrambot.user.vacancy.UserVacancy;
import com.zhevlakov.findjobtelegrambot.user.vacancy.UserVacancyService;
import com.zhevlakov.findjobtelegrambot.vacancy.VacancyResponseFactory;
import com.zhevlakov.findjobtelegrambot.vacancy.VacancySearchFilter;
import com.zhevlakov.findjobtelegrambot.vacancy.VacancyStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FavouriteCommandHandler implements CommandHandler {
    private final UserVacancyService userVacancyService;
    private final VacancyResponseFactory vacancyResponseFactory;

    public FavouriteCommandHandler(
            UserVacancyService userVacancyService,
            VacancyResponseFactory vacancyResponseFactory
    ) {
        this.userVacancyService = userVacancyService;
        this.vacancyResponseFactory = vacancyResponseFactory;
    }

    @Override
    public List<BotResponse> handle(Message message) {
        VacancySearchFilter filter = new VacancySearchFilter(
                null,
                null
        );
        var userId = message.chat().id();

        List<UserVacancy> vacancies = userVacancyService.getUserVacanciesByStatus(filter, userId, VacancyStatus.FAVOURITE);

        return vacancyResponseFactory.buildResponse(
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

    @Override
    public CommandHandlerName getCommandHandlerName() {
        return CommandHandlerName.FAVOURITE;
    }
}
