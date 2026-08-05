package com.zhevlakov.findjobtelegrambot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.zhevlakov.findjobtelegrambot.command.CommandDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UpdateHandler {
    private final CommandDispatcher commandDispatcher;
    private final MessageSenderService senderService;
    private final Logger log = LoggerFactory.getLogger(UpdateHandler.class);

    public UpdateHandler(
            CommandDispatcher commandDispatcher,
            MessageSenderService senderService
    ) {
        this.commandDispatcher = commandDispatcher;
        this.senderService = senderService;
    }

    public void handleUpdate(Update update) {
        try {
            process(update);
        } catch (Exception e) {
            log.info("Во время работы произошла ошибка", e);
            sendUserErrorMessage(update.message().chat().id());
        }
    }

    public void process(Update update) {
        if (update.message() != null) {
            var message = update.message();
            if (message.text().startsWith("/")) {
                commandDispatcher.processCommand(message);
            }
        }
    }

    private void sendUserErrorMessage(Long userId) {
        SendMessage errorRequest = new SendMessage(userId, "Произошла ошибка.");
        senderService.sendMessage(errorRequest);
    }

    private boolean isCommand(Update update) {
        return true;
    }

}
