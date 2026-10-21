package com.zhevlakov.findjobtelegrambot.command.handler.vacancy;

import com.pengrad.telegrambot.model.Message;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.command.CommandHandler;
import com.zhevlakov.findjobtelegrambot.command.CommandHandlerName;
import com.zhevlakov.findjobtelegrambot.fsm.FsmStates;
import com.zhevlakov.findjobtelegrambot.user.UserService;
import com.zhevlakov.findjobtelegrambot.vacancy.ui.VacancyUiManager;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchVacanciesCommandHandler implements CommandHandler {
    private final UserService userService;
    private final VacancyUiManager vacancyUiManager;

    public SearchVacanciesCommandHandler(
            UserService userService,
            VacancyUiManager vacancyUiManager
    ) {
        this.userService = userService;
        this.vacancyUiManager = vacancyUiManager;
    }

    @Override
    public List<BotResponse> handle(Message message) {
        Long userId = message.chat().id();

        userService.changeUserState(userId, FsmStates.FREE);

        return vacancyUiManager.outputVacancies(userId);
    }

    @Override
    public CommandHandlerName getCommandHandlerName() {
        return CommandHandlerName.CONTINUE_SEARCH_VACANCIES;
    }
}
