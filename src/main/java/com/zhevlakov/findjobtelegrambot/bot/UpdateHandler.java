package com.zhevlakov.findjobtelegrambot.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.zhevlakov.findjobtelegrambot.callback.CallbackDispatcher;
import com.zhevlakov.findjobtelegrambot.command.CommandDispatcher;
import com.zhevlakov.findjobtelegrambot.fsm.FsmDispatcher;
import com.zhevlakov.findjobtelegrambot.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UpdateHandler {
    private final CommandDispatcher commandDispatcher;
    private final CallbackDispatcher callbackDispatcher;
    private final MessageSenderService senderService;
    private final UserService userService;
    private final FsmDispatcher fsmDispatcher;
    private final BotResponseMapper responseMapper;
    private final Logger log = LoggerFactory.getLogger(UpdateHandler.class);

    public UpdateHandler(
            CallbackDispatcher callbackDispatcher,
            CommandDispatcher commandDispatcher,
            MessageSenderService senderService,
            UserService userService,
            FsmDispatcher fsmDispatcher,
            BotResponseMapper responseMapper
    ) {
        this.callbackDispatcher = callbackDispatcher;
        this.commandDispatcher = commandDispatcher;
        this.senderService = senderService;
        this.userService = userService;
        this.fsmDispatcher = fsmDispatcher;
        this.responseMapper = responseMapper;
    }

    public void handleUpdate(Update update) {
        try {
            var botResponse = process(update);
            var request = responseMapper.toRequest(botResponse);
            senderService.sendMessage(request);
        } catch (Exception e) {
            log.info("Во время работы произошла ошибка", e);
            sendUserErrorMessage(update.message().chat().id());
        }
    }

    public BotResponse process(Update update) {
        if (update.callbackQuery() != null) {
            var callback = update.callbackQuery();
            return callbackDispatcher.processCallback(callback);
        }

        if (update.message() != null) {
            var message = update.message();
            var chatId = message.chat().id();

            if (commandDispatcher.isCommand(message)) {
                return commandDispatcher.processCommand(message);
            }

            if (!userService.isUserFree(chatId)) {
                return fsmDispatcher.processFsmCommand(message);
            }
        }

        return BotResponse.post(update.message().chat().id(), "Рядовое сообщение.");
    }

    private void sendUserErrorMessage(Long userId) {
        SendMessage errorRequest = new SendMessage(userId, "Произошла ошибка.");
        senderService.sendMessage(errorRequest);
    }
}
