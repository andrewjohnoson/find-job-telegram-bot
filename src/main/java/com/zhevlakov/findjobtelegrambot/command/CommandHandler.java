package com.zhevlakov.findjobtelegrambot.command;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.AbstractSendRequest;

public interface CommandHandler {
    AbstractSendRequest<?> handle(Message message); // заменить войд на какой-то класс ответа
    CommandHandlerName getCommandHandlerName();
}
