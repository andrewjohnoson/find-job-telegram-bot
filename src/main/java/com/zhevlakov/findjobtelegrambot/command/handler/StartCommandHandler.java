package com.zhevlakov.findjobtelegrambot.command.handler;

import com.pengrad.telegrambot.model.Message;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardGenerator;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.command.CommandHandler;
import com.zhevlakov.findjobtelegrambot.command.CommandHandlerName;
import com.zhevlakov.findjobtelegrambot.user.UserService;
import org.springframework.stereotype.Component;

@Component
public class StartCommandHandler implements CommandHandler {
    private final static String TEXT_RESPONSE = """
                Привет %s. Я бот, который помогает искать работу.
            
            Я присылаю уведомления в определённый промежуток времени.
            
            Ты можешь задать запрос на поиск вакансий, посмотреть в Избранном те, которые ты отметил, а также перестать получать уведомления.
            """;
    private final UserService userService;
    private final KeyboardGenerator keyboardGenerator;

    public StartCommandHandler(UserService userService,
                               KeyboardGenerator keyboardGenerator
    ) {
        this.userService = userService;
        this.keyboardGenerator = keyboardGenerator;
    }

    @Override
    public BotResponse handle(Message message) {
        var chatId = message.chat().id();
        if (userService.haveUser(chatId) && !userService.isUserFree(chatId)) {
            return BotResponse.error(chatId, "Данная операция в данный момент не доступна.");
        }

        var userTag = message.from().username();
        var user = userService.createNewUser(chatId, userTag);

        return BotResponse.post(user.getChatId(), TEXT_RESPONSE.formatted(user.getUserTag()),
                keyboardGenerator.getStartCommandKeyboard());
    }

    @Override
    public CommandHandlerName getCommandHandlerName() {
        return CommandHandlerName.START;
    }
}
