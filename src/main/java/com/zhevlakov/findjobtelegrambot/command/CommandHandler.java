package com.zhevlakov.findjobtelegrambot.command;

import com.pengrad.telegrambot.model.Message;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;

import java.util.List;

public interface CommandHandler {
    List<BotResponse> handle(Message message);
    CommandHandlerName getCommandHandlerName();
}
