package com.zhevlakov.findjobtelegrambot.bot;

import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class BotResponseMapper {
    public AbstractSendRequest<?> toRequest(BotResponse botResponse) {
        if (botResponse == null) {
            throw new IllegalArgumentException("Передан пустой ответ.");
        }

        SendMessage request = new SendMessage(botResponse.chatId(), botResponse.text());
        if (botResponse.keyboard() != null) {
            request.replyMarkup(botResponse.keyboard());
        }

        return request;
    }
}
