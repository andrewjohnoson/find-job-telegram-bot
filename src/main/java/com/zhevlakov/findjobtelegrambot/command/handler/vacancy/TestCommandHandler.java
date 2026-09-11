package com.zhevlakov.findjobtelegrambot.command.handler.vacancy;

import com.pengrad.telegrambot.model.Message;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.command.CommandHandler;
import com.zhevlakov.findjobtelegrambot.command.CommandHandlerName;
import com.zhevlakov.findjobtelegrambot.vacancy.playwrightscrapper.HhPlaywrightScrapper;
import com.zhevlakov.findjobtelegrambot.vacancy.provider.TestProvider;
import com.zhevlakov.findjobtelegrambot.vacancy.query.mapper.HhQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestCommandHandler implements CommandHandler {
    private final TestProvider testProvider;
    private final HhQueryBuilder hhQueryBuilder;
    private final HhPlaywrightScrapper hhPlaywrightScrapper;
    private final Logger log = LoggerFactory.getLogger(TestCommandHandler.class);

    public TestCommandHandler(TestProvider testProvider,
                              HhQueryBuilder hhQueryBuilder,
                              HhPlaywrightScrapper hhPlaywrightScrapper
    ) {
        this.testProvider = testProvider;
        this.hhQueryBuilder = hhQueryBuilder;
        this.hhPlaywrightScrapper = hhPlaywrightScrapper;
    }

    @Override
    public List<BotResponse> handle(Message message) {
        var userId = message.chat().id();
//        return testProvider.fetchVacancies(userId);

//        log.info("Vacancies {}", hhPlaywrightScrapper.fetchVacancies());

        hhQueryBuilder.test();

        return BotResponse.asList(BotResponse.post(userId, "Тест"));
    }

    @Override
    public CommandHandlerName getCommandHandlerName() {
        return CommandHandlerName.TEST;
    }
}
