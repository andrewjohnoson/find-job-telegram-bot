package com.zhevlakov.findjobtelegrambot.command.handler.vacancy;

import com.pengrad.telegrambot.model.Message;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.command.CommandHandler;
import com.zhevlakov.findjobtelegrambot.command.CommandHandlerName;
import com.zhevlakov.findjobtelegrambot.vacancy.provider.TestProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestCommandHandler implements CommandHandler {
    private final TestProvider testProvider;

    public TestCommandHandler(TestProvider testProvider) {
        this.testProvider = testProvider;
    }

    @Override
    public List<BotResponse> handle(Message message) {
        var userId = message.chat().id();
        return testProvider.fetchVacancies(userId);

//        return BotResponse.asList(BotResponse.post(userId, "Тест"));
    }

    @Override
    public CommandHandlerName getCommandHandlerName() {
        return CommandHandlerName.TEST;
    }
}
