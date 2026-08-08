package com.zhevlakov.findjobtelegrambot.bot;

import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class BotResponseMapper {
    public AbstractSendRequest<?> toRequest(BotResponse botResponse) {
        SendMessage request = new SendMessage(botResponse.chatId(), botResponse.text());
        if (botResponse.keyboard() != null) {
            request.replyMarkup(botResponse.keyboard());
        }

        return request;
    }
}
