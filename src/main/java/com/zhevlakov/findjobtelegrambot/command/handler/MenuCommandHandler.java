package com.zhevlakov.findjobtelegrambot.command.handler;

import com.pengrad.telegrambot.model.Message;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.command.CommandHandler;
import com.zhevlakov.findjobtelegrambot.command.CommandHandlerName;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardGenerator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MenuCommandHandler implements CommandHandler {
    private final KeyboardGenerator keyboardGenerator;

    public MenuCommandHandler(KeyboardGenerator keyboardGenerator) {
        this.keyboardGenerator = keyboardGenerator;
    }

    @Override
    public List<BotResponse> handle(Message message) {
        var userId = message.chat().id();
        var text = "Выберите действие";
        var keyboard = keyboardGenerator.getStartCommandKeyboard();

        return List.of(BotResponse.post(userId, text, keyboard));
    }

    @Override
    public CommandHandlerName getCommandHandlerName() {
        return CommandHandlerName.MENU;
    }
}
