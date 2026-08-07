package com.zhevlakov.findjobtelegrambot.bot;

import com.pengrad.telegrambot.request.AbstractSendRequest;
import org.springframework.stereotype.Component;

@Component
public class BotResponseMapper {
    public AbstractSendRequest<?> toRequest(BotResponse botResponse) {
        return null;
    }
}
