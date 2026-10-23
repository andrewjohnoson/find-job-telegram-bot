package com.zhevlakov.findjobtelegrambot.bot;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.zhevlakov.findjobtelegrambot.callback.CallbackDispatcher;
import com.zhevlakov.findjobtelegrambot.command.CommandDispatcher;
import com.zhevlakov.findjobtelegrambot.fsm.FsmDispatcher;
import com.zhevlakov.findjobtelegrambot.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdateHandler {
    private final CommandDispatcher commandDispatcher;
    private final CallbackDispatcher callbackDispatcher;
    private final TelegramMessageSenderService senderService;
    private final UserService userService;
    private final FsmDispatcher fsmDispatcher;
    private final BotResponseMapper responseMapper;
    private final Logger log = LoggerFactory.getLogger(UpdateHandler.class);

    public UpdateHandler(
            CallbackDispatcher callbackDispatcher,
            CommandDispatcher commandDispatcher,
            TelegramMessageSenderService senderService,
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
        var chatId = getChatId(update);

        try {
            var botResponseList = process(update);
            for (BotResponse botResponse : botResponseList) {
                if (botResponse.updatePrevKeyboard()) {
                    continue;
                }
                var request = responseMapper.toRequest(botResponse);
                senderService.sendMessage(request);
            }
        } catch (NullPointerException e) {
            if (update.inlineQuery() != null) {
                return;
            }

            log.info("В одном из обработчиков возникло исключение NullPointerException. ", e);
            sendUserErrorMessage(chatId);
        } catch (Exception e) {
            if (update.inlineQuery() != null) {
                return;
            }

            log.error("Во время работы произошла ошибка", e);
            sendUserErrorMessage(chatId);
        }
    }

    public List<BotResponse> process(Update update) {
        if (update.callbackQuery() != null) {
            var callback = update.callbackQuery();
            try {
                var response = callbackDispatcher.processCallback(callback);
                updatingOrRemovingKeyboard(response, callback);
                return response;
            } catch (NullPointerException e) {
                throw new NullPointerException();
            }
        }

        if (update.message() != null) {
            var message = update.message();
            var chatId = message.chat().id();

            if (commandDispatcher.isCommand(message)) {
                try {
                    return commandDispatcher.processCommand(message);
                } catch (NullPointerException e) {
                    throw new NullPointerException();
                }
            }

            if (!userService.isUserFree(chatId)) {
                return BotResponse.asList(fsmDispatcher.processFsmCommand(message));
            }
        }

        return BotResponse.asList(BotResponse.post(update.message().chat().id(), "Рядовое сообщение."));
    }

    private void sendUserErrorMessage(Long userId) {
        SendMessage errorRequest = new SendMessage(userId, "Произошла ошибка.");
        senderService.sendMessage(errorRequest);
    }

    private void updatePrevKeyboard(CallbackQuery callbackQuery, BotResponse response) {
        var maybeMessage = callbackQuery.maybeInaccessibleMessage();
        if (maybeMessage instanceof Message message) {
            var lastText = message.text();
            var messageId = message.messageId();
            var chatId = callbackQuery.from().id();
            var editRequest = new EditMessageText(chatId, messageId, lastText)
                    .replyMarkup((InlineKeyboardMarkup) response.keyboard());
            senderService.changePrevMessage(editRequest);
        }
    }

    private void removeKeyboardFromPrev(CallbackQuery callbackQuery, BotResponse response) {
        var maybeMessage = callbackQuery.maybeInaccessibleMessage();
        if (maybeMessage instanceof Message message) {
            var keyboard = message.replyMarkup();

            boolean shouldRemove = (response == null) || response.removeKeyboard();

            if (keyboard != null && shouldRemove) {
                var lastText = message.text();
                var messageId = message.messageId();
                var chatId = callbackQuery.from().id();
                var editRequest = new EditMessageText(chatId, messageId, lastText);
                senderService.changePrevMessage(editRequest);
            }
        }
    }

    private void updatingOrRemovingKeyboard(List<BotResponse> response, CallbackQuery callback) {
        if (!response.isEmpty()) {
                BotResponse firstResponse = response.getFirst();
                if (firstResponse.updatePrevKeyboard()) {
                    updatePrevKeyboard(callback, firstResponse);
                } else {
                    removeKeyboardFromPrev(callback, firstResponse);
                }

                if (firstResponse.removePost()) {
                    removePost(callback, firstResponse);
                }
            } else {
                removeKeyboardFromPrev(callback, null);
            }
    }

    private void removePost(CallbackQuery callbackQuery, BotResponse response) {
        var maybeMessage = callbackQuery.maybeInaccessibleMessage();
        if (maybeMessage instanceof Message message) {
            var messageId = message.messageId();
            var chatId = callbackQuery.from().id();
            var deleteMessage = new DeleteMessage(chatId, messageId);
            senderService.removeMessage(deleteMessage);
        }
    }

    private Long getChatId(Update update) {
        if (update.message() != null && update.message().chat() != null) {
            return update.message().chat().id();
        }

        if (update.callbackQuery() != null && update.callbackQuery().from() != null) {
            return update.callbackQuery().from().id();
        }

        if (update.editedMessage() != null && update.editedMessage().chat() != null) {
            return update.editedMessage().chat().id();
        }

        return null;
    }
}
