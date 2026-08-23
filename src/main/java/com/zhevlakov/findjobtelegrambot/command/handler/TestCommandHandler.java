package com.zhevlakov.findjobtelegrambot.command.handler;

import com.pengrad.telegrambot.model.Message;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.command.CommandHandler;
import com.zhevlakov.findjobtelegrambot.command.CommandHandlerName;
import com.zhevlakov.findjobtelegrambot.vacancy.provider.TestProvider;
import org.springframework.stereotype.Component;

@Component
public class TestCommandHandler implements CommandHandler {
    private final TestProvider testProvider;

    public TestCommandHandler(TestProvider testProvider) {
        this.testProvider = testProvider;
    }

    @Override
    public BotResponse handle(Message message) {
        var userId = message.chat().id();
        testProvider.fetchVacancies(userId);

        return BotResponse.post(userId, "Тест");
    }

    @Override
    public CommandHandlerName getCommandHandlerName() {
        return CommandHandlerName.TEST;
    }
}
