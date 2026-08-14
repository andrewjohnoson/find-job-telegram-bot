package com.zhevlakov.findjobtelegrambot.command.handler;

import com.pengrad.telegrambot.model.Message;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.command.CommandHandler;
import com.zhevlakov.findjobtelegrambot.command.CommandHandlerName;
import com.zhevlakov.findjobtelegrambot.user.UserService;
import com.zhevlakov.findjobtelegrambot.user.query.UserQueryService;
import org.springframework.stereotype.Component;

@Component
public class CurrentQueryCommandHandler implements CommandHandler {
    private final UserService userService;
    private final UserQueryService queryService;

    public CurrentQueryCommandHandler(UserService userService,
                                      UserQueryService queryService
    ) {
        this.userService = userService;
        this.queryService = queryService;
    }

    @Override
    public BotResponse handle(Message message) {
        var chatId = message.chat().id();

        if (!userService.haveUser(chatId)) {
            return BotResponse.error(chatId, "Произошла ошибка");
        }

        if (!queryService.hasQuery(chatId)) {
            return BotResponse.post(chatId, "Пользоватлеь не имеет запроса");
        }

        var query = queryService.getByChatId(chatId);
        return BotResponse.post(chatId, query.toString());
    }

    @Override
    public CommandHandlerName getCommandHandlerName() {
        return CommandHandlerName.CURRENT_QUERY;
    }
}
