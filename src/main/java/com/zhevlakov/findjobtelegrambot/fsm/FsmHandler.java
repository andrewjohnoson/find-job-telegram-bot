package com.zhevlakov.findjobtelegrambot.fsm;

import com.pengrad.telegrambot.model.Message;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.user.UserEntity;
import org.springframework.stereotype.Component;

@Component
public interface FsmHandler {
    BotResponse handle(Message message, UserEntity user);
    UserEntity changeState(UserEntity user);
    FsmStates getState();
}
