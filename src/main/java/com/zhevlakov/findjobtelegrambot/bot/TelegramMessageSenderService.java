package com.zhevlakov.findjobtelegrambot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TelegramMessageSenderService implements MessageSenderService {
    private final TelegramBot bot;
    private final Logger log = LoggerFactory.getLogger(TelegramMessageSenderService.class);

    public TelegramMessageSenderService(
            TelegramBot bot,
            BotResponseMapper responseMapper
    ) {
        this.bot = bot;
    }

    @Override
    public void sendMessage(AbstractSendRequest<?> request) {
        SendResponse response = bot.execute(request);
        log.info("Is execution successful={}", response.isOk());
    }
}
