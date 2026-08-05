package com.zhevlakov.findjobtelegrambot;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TelegramBotInitializer {
    private final TelegramBot bot;
    private final CustomUpdatesListener updatesListener;
    private final CustomExceptionHandler exceptionHandler;

    public TelegramBotInitializer(
            TelegramBot bot,
            CustomUpdatesListener updatesListener,
            CustomExceptionHandler exceptionHandler
    ) {
        this.bot = bot;
        this.updatesListener = updatesListener;
        this.exceptionHandler = exceptionHandler;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void linkHandlers() {
        bot.setUpdatesListener(updatesListener, exceptionHandler);
    }
}
