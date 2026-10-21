package com.zhevlakov.findjobtelegrambot.command.handler.vacancy;

import com.pengrad.telegrambot.model.Message;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.callback.CallbackContent;
import com.zhevlakov.findjobtelegrambot.callback.CallbackMapper;
import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;
import com.zhevlakov.findjobtelegrambot.command.CommandHandler;
import com.zhevlakov.findjobtelegrambot.command.CommandHandlerName;
import com.zhevlakov.findjobtelegrambot.fsm.FsmStates;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardButtonContent;
import com.zhevlakov.findjobtelegrambot.user.UserService;
import com.zhevlakov.findjobtelegrambot.user.vacancy.UserVacancy;
import com.zhevlakov.findjobtelegrambot.user.vacancy.UserVacancyService;
import com.zhevlakov.findjobtelegrambot.vacancy.ui.VacancyResponseFactory;
import com.zhevlakov.findjobtelegrambot.vacancy.VacancySearchFilter;
import com.zhevlakov.findjobtelegrambot.vacancy.VacancyStatus;
import com.zhevlakov.findjobtelegrambot.vacancy.ui.VacancyUiManager;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FavouriteCommandHandler implements CommandHandler {
    private final UserVacancyService userVacancyService;
    private final VacancyResponseFactory vacancyResponseFactory;
    private final CallbackMapper callbackMapper;
    private final VacancyUiManager vacancyUiManager;
    private final UserService userService;

    public FavouriteCommandHandler(
            UserVacancyService userVacancyService,
            VacancyResponseFactory vacancyResponseFactory,
            CallbackMapper callbackMapper,
            VacancyUiManager vacancyUiManager,
            UserService userService
    ) {
        this.userVacancyService = userVacancyService;
        this.vacancyResponseFactory = vacancyResponseFactory;
        this.callbackMapper = callbackMapper;
        this.vacancyUiManager = vacancyUiManager;
        this.userService = userService;
    }

    @Override
    public List<BotResponse> handle(Message message) {
        VacancySearchFilter filter = new VacancySearchFilter(
                null,
                null
        );

        var userId = message.chat().id();
        userService.changeUserState(userId, FsmStates.WATCH_FAVOURITE);

        return vacancyUiManager.outputFavouriteVacancies(userId, filter);
    }

    @Override
    public CommandHandlerName getCommandHandlerName() {
        return CommandHandlerName.FAVOURITE;
    }
}
