package com.zhevlakov.findjobtelegrambot.fsm;

import com.pengrad.telegrambot.model.Message;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FsmDispatcher {
    private final QueryFsmWizardService wizardService;

    @Autowired
    public FsmDispatcher(
            QueryFsmWizardService wizardService
    ) {
        this.wizardService = wizardService;
    }

    public BotResponse processFsmCommand(Message message) {
        var chatId = message.chat().id();
        String input = message.text();

        return wizardService.processStep(chatId, input);
    }
}
