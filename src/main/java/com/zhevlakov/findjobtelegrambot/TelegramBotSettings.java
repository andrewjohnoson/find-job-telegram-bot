package com.zhevlakov.findjobtelegrambot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TelegramBotSettings {
    private final Logger log = LoggerFactory.getLogger(TelegramBotSettings.class);
    private final TelegramBot bot;
    private final CustomUpdatesListener updatesListener;
    private final CustomUpdatesListenerExceptionHandler exceptionHandler;

    public TelegramBotSettings(
            @Value("${bot.api.token}") String botToken,
            CustomUpdatesListener updatesListener,
            CustomUpdatesListenerExceptionHandler exceptionHandler
    ) {
        this.updatesListener = updatesListener;
        this.exceptionHandler = exceptionHandler;
        bot = new TelegramBot(botToken);
        bot.setUpdatesListener(this.updatesListener, this.exceptionHandler);
    }

    private void send(Update update, String message) {
        SendMessage request = new SendMessage(update.message().chat().id(), message);

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
}
