package com.zhevlakov.findjobtelegrambot;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfig {

    @Bean
    public TelegramBot telegramBot (
            @Value("${bot.api.token}") String botToken
    ) {
        return new TelegramBot(botToken);
    }
}
