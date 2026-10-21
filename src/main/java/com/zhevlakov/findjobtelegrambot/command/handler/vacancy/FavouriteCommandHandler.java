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
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FavouriteCommandHandler implements CommandHandler {
    private final VacancyUiManager vacancyUiManager;
    private final UserService userService;
    private final UserVacancyService userVacancyService;

    public FavouriteCommandHandler(
            VacancyUiManager vacancyUiManager,
            UserService userService,
            UserVacancyService userVacancyService
    ) {
        this.vacancyUiManager = vacancyUiManager;
        this.userService = userService;
        this.userVacancyService = userVacancyService;
    }

    @Override
    @Transactional
    public List<BotResponse> handle(Message message) {
        var userId = message.chat().id();
        userService.changeUserState(userId, FsmStates.WATCH_FAVOURITE);
        userVacancyService.makeVisible(userId, VacancyStatus.FAVOURITE);

        return vacancyUiManager.outputFavouriteVacancies(userId);
    }

    @Override
    public CommandHandlerName getCommandHandlerName() {
        return CommandHandlerName.FAVOURITE;
    }
}
