package com.zhevlakov.findjobtelegrambot.command.handler;

import com.pengrad.telegrambot.model.Message;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.command.CommandHandler;
import com.zhevlakov.findjobtelegrambot.command.CommandHandlerName;
import com.zhevlakov.findjobtelegrambot.fsm.QueryFsmWizardService;
import com.zhevlakov.findjobtelegrambot.user.UserService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NewQueryCommandHandler implements CommandHandler {
    private final UserService userService;
    private final QueryFsmWizardService wizardService;

    public NewQueryCommandHandler(
            UserService userService,
            QueryFsmWizardService wizardService
    ) {
        this.userService = userService;
        this.wizardService = wizardService;
    }

    @Override
    public List<BotResponse> handle(Message message) {
        var chatId = message.chat().id();
        if (!userService.isUserFree(chatId)) {
            return BotResponse.asList(BotResponse.error(chatId, "Данная операция в данный момент не доступна."));
        }

        var botResponse = wizardService.start(chatId);
        return BotResponse.asList(botResponse);
    }

    @Override
    public CommandHandlerName getCommandHandlerName() {
        return CommandHandlerName.NEW_QUERY;
    }
}
