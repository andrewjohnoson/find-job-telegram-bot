package com.zhevlakov.findjobtelegrambot.command.handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.zhevlakov.findjobtelegrambot.command.CommandHandler;
import com.zhevlakov.findjobtelegrambot.command.CommandHandlerName;
import com.zhevlakov.findjobtelegrambot.fsm.FsmDispatcher;
import com.zhevlakov.findjobtelegrambot.user.UserService;
import org.springframework.stereotype.Component;

@Component
public class NewQueryCommandHandler implements CommandHandler {
    private final UserService userService;
    private final FsmDispatcher fsmDispatcher;

    public NewQueryCommandHandler(
            UserService userService,
            FsmDispatcher fsmDispatcher
    ) {
        this.userService = userService;
        this.fsmDispatcher = fsmDispatcher;
    }

    @Override
    public AbstractSendRequest<?> handle(Message message) {
        var chatId = message.chat().id();
        if (!userService.isUserFree(chatId)) {
            return new SendMessage(chatId, "Данная операция в данный момент не доступна.");
        }

        return fsmDispatcher.processFsmCommand(message);
    }

    @Override
    public CommandHandlerName getCommandHandlerName() {
        return CommandHandlerName.NEW_QUERY;
    }
}
