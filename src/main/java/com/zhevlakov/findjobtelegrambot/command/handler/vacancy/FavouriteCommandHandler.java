package com.zhevlakov.findjobtelegrambot.command.handler.vacancy;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.command.CommandHandler;
import com.zhevlakov.findjobtelegrambot.command.CommandHandlerName;
import com.zhevlakov.findjobtelegrambot.fsm.FsmStates;
import com.zhevlakov.findjobtelegrambot.user.UserService;
import com.zhevlakov.findjobtelegrambot.user.vacancy.UserVacancyService;
import com.zhevlakov.findjobtelegrambot.vacancy.VacancyStatus;
import com.zhevlakov.findjobtelegrambot.vacancy.ui.VacancyResponseFactory;
import com.zhevlakov.findjobtelegrambot.vacancy.ui.VacancyUiManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FavouriteCommandHandler implements CommandHandler {
    private final VacancyUiManager vacancyUiManager;
    private final UserService userService;
    private final UserVacancyService userVacancyService;
    private final VacancyResponseFactory vacancyResponseFactory;

    public FavouriteCommandHandler(
            VacancyUiManager vacancyUiManager,
            UserService userService,
            UserVacancyService userVacancyService,
            VacancyResponseFactory vacancyResponseFactory
    ) {
        this.vacancyUiManager = vacancyUiManager;
        this.userService = userService;
        this.userVacancyService = userVacancyService;
        this.vacancyResponseFactory = vacancyResponseFactory;
    }

    @Override
    @Transactional
    public List<BotResponse> handle(Message message) {
        var userId = message.chat().id();
        if (userService.hasState(userId, FsmStates.WATCH_FAVOURITE)) {
            Keyboard keyboard =
                    vacancyResponseFactory.generateContinueVacancySearchingKeyboard();
            return BotResponse.asList(
                    BotResponse.post(
                            userId,
                            "Вы уже просматриваете изрбанные вакансии.",
                            keyboard
                    )
            );
        }

        userService.changeUserState(userId, FsmStates.WATCH_FAVOURITE);
        userVacancyService.makeVisible(userId, VacancyStatus.FAVOURITE);

        return vacancyUiManager.outputFavouriteVacancies(userId);
    }

    @Override
    public CommandHandlerName getCommandHandlerName() {
        return CommandHandlerName.FAVOURITE;
    }
}
