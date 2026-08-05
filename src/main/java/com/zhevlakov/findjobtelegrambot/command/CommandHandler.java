package com.zhevlakov.findjobtelegrambot.command;

import com.pengrad.telegrambot.model.Message;

public interface CommandHandler {
    void handle(Message message); // заменить войд на какой-то класс ответа
    CommandHandlerName getCommandHandlerName();
}
