package com.zhevlakov.findjobtelegrambot.command.handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.zhevlakov.findjobtelegrambot.command.CommandHandler;
import com.zhevlakov.findjobtelegrambot.command.CommandHandlerName;

public class StopNotificationsCommandHandler implements CommandHandler {

    @Override
    public AbstractSendRequest<?> handle(Message message) {
        return null;
    }

    @Override
    public CommandHandlerName getCommandHandlerName() {
        return CommandHandlerName.STOP_NOTIFICATIONS;
    }
}
