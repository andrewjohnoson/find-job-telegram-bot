package com.zhevlakov.findjobtelegrambot.command.handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.zhevlakov.findjobtelegrambot.command.CommandHandler;
import com.zhevlakov.findjobtelegrambot.command.CommandHandlerName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartCommandHandlerHandler implements CommandHandler {
    private final Logger log = LoggerFactory.getLogger(StartCommandHandlerHandler.class); // потом убрать


    @Override
    public void handle(Message message) {
        SendMessage request = new SendMessage(message.chat().id(), "message");

        KeyboardButton[] buttons = {
                new KeyboardButton("Новый запрос"),
                new KeyboardButton("Избранное"),
                new KeyboardButton("Перестать искать")
        };
        Keyboard replyKeyboardMarkup = new ReplyKeyboardMarkup(buttons).resizeKeyboard(true);

        request.replyMarkup(replyKeyboardMarkup);
        SendResponse sendResponse = bot.execute(request);
        log.info("Is successful={}", sendResponse.isOk());
    }

    @Override
    public CommandHandlerName getCommandHandlerName() {
        return CommandHandlerName.START;
    }
}
