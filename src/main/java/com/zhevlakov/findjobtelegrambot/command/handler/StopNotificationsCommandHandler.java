package com.zhevlakov.findjobtelegrambot.command.handler;

import com.pengrad.telegrambot.model.Message;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.command.CommandHandler;
import com.zhevlakov.findjobtelegrambot.command.CommandHandlerName;
import org.springframework.stereotype.Component;

@Component
public class StopNotificationsCommandHandler implements CommandHandler {

    @Override
    public BotResponse handle(Message message) {
        return null;
    }

    @Override
    public CommandHandlerName getCommandHandlerName() {
        return CommandHandlerName.STOP_NOTIFICATIONS;
    }
}
