package com.zhevlakov.findjobtelegrambot.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
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
            var request = process(update);
            senderService.sendMessage(request);
        } catch (Exception e) {
            log.info("Во время работы произошла ошибка", e);
            sendUserErrorMessage(update.message().chat().id());
        }
    }

    public AbstractSendRequest<?> process(Update update) {
        if (update.message() != null) {
            var message = update.message();
            if (commandDispatcher.isCommand(message)) {
                return commandDispatcher.processCommand(message);
            }
        }

        return null;
    }

    private void sendUserErrorMessage(Long userId) {
        SendMessage errorRequest = new SendMessage(userId, "Произошла ошибка.");
        senderService.sendMessage(errorRequest);
    }

    private boolean isCommand(Update update) {
        return true;
    }

}
