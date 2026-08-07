package com.zhevlakov.findjobtelegrambot.command;

import com.pengrad.telegrambot.model.Message;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;

public interface CommandHandler {
    BotResponse handle(Message message); // заменить войд на какой-то класс ответа
    CommandHandlerName getCommandHandlerName();
}
