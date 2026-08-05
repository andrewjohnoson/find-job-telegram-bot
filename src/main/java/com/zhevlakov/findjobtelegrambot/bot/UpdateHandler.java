package com.zhevlakov.findjobtelegrambot.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.zhevlakov.findjobtelegrambot.callback.CallbackDispatcher;
import com.zhevlakov.findjobtelegrambot.command.CommandDispatcher;
import com.zhevlakov.findjobtelegrambot.fsm.FsmDispatcher;
import com.zhevlakov.findjobtelegrambot.fsm.FsmStates;
import com.zhevlakov.findjobtelegrambot.user.UserEntity;
import com.zhevlakov.findjobtelegrambot.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class UpdateHandler {
    private final CommandDispatcher commandDispatcher;
    private final CallbackDispatcher callbackDispatcher;
    private final MessageSenderService senderService;
    private final FsmDispatcher fsmDispatcher;
    private final UserRepository userRepository;
    private final Logger log = LoggerFactory.getLogger(UpdateHandler.class);

    public UpdateHandler(
            CommandDispatcher commandDispatcher,
            CallbackDispatcher callbackDispatcher,
            MessageSenderService senderService,
            FsmDispatcher fsmDispatcher,
            UserRepository userRepository
    ) {
        this.commandDispatcher = commandDispatcher;
        this.callbackDispatcher = callbackDispatcher;
        this.senderService = senderService;
        this.fsmDispatcher = fsmDispatcher;
        this.userRepository = userRepository;
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
            var chatId = message.chat().id();
            if (!userRepository.existsById(chatId)) {
                var user = new UserEntity(
                        chatId,
                        update.message().from().username(),
                        FsmStates.FREE,
                        null);
                userRepository.save(user);
            }

            var user = userRepository.findById(chatId).orElseThrow(() ->
                    new NoSuchElementException("Не был найден пользователь: chatId=%d".formatted(chatId)));

            if (!user.getState().equals(FsmStates.FREE)) {
                return fsmDispatcher.processFsmCommand(user, update);
            }

            if (commandDispatcher.isCommand(message)) {
                return commandDispatcher.processCommand(message);
            }
        }

        if (update.callbackQuery() != null) {
            var callback = update.callbackQuery();
            return callbackDispatcher.processCallback(callback);
        }

        return null;
    }

    private void sendUserErrorMessage(Long userId) {
        SendMessage errorRequest = new SendMessage(userId, "Произошла ошибка.");
        senderService.sendMessage(errorRequest);
    }
}
