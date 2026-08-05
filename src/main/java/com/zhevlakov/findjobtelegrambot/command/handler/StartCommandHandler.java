package com.zhevlakov.findjobtelegrambot.command.handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.zhevlakov.findjobtelegrambot.command.CommandHandler;
import com.zhevlakov.findjobtelegrambot.command.CommandHandlerName;
import org.springframework.stereotype.Component;

@Component
public class StartCommandHandler implements CommandHandler {
    private final static String TEXT_RESPONSE = """
                Привет %s. Я бот, который помогает искать работу.
            
            Я присылаю уведомления в определённый промежуток времени.
            
            Ты можешь задать запрос на поиск вакансий, посмотреть в Избранном те, которые ты отметил, а также перестать получать уведомления.
            """;

    @Override
    public AbstractSendRequest<?> handle(Message message) {
        Long chatId = message.chat().id();
        String userName = message.from().username();
        AbstractSendRequest<SendMessage> request = new SendMessage(chatId, TEXT_RESPONSE.formatted(userName));

        KeyboardButton[] buttons = {
                new KeyboardButton("Новый запрос"),
                new KeyboardButton("Избранное"),
                new KeyboardButton("Перестать искать")
        };

        Keyboard replyKeyboardMarkup = new ReplyKeyboardMarkup(buttons).resizeKeyboard(true);
        request.replyMarkup(replyKeyboardMarkup);

        return request;
    }

    @Override
    public CommandHandlerName getCommandHandlerName() {
        return CommandHandlerName.START;
    }
}
