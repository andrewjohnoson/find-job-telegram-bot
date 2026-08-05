package com.zhevlakov.findjobtelegrambot;

import com.pengrad.telegrambot.request.AbstractSendRequest;

public interface MessageSenderService {
    void sendMessage(AbstractSendRequest<?> request);
}
