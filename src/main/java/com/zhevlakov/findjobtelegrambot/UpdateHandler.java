package com.zhevlakov.findjobtelegrambot;

import com.pengrad.telegrambot.model.Update;
import com.zhevlakov.findjobtelegrambot.command.CommandDispatcher;
import org.springframework.stereotype.Component;

@Component
public class UpdateHandler {
    private final CommandDispatcher commandDispatcher;

    public UpdateHandler(
            CommandDispatcher commandDispatcher
    ) {
        this.commandDispatcher = commandDispatcher;
    }

    public void handleUpdate(Update update) {
        if (update.message() != null) {
            var message = update.message();
            if (message.text().startsWith("/")) {
                commandDispatcher.processCommand(message);
            }
        }
    }

    private boolean isCommand(Update update) {
        return true;
    }

}
