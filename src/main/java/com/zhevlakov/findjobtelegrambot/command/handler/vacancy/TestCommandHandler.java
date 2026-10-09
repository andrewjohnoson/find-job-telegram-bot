package com.zhevlakov.findjobtelegrambot.command.handler.vacancy;

import com.pengrad.telegrambot.model.Message;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.command.CommandHandler;
import com.zhevlakov.findjobtelegrambot.command.CommandHandlerName;
import com.zhevlakov.findjobtelegrambot.user.query.UserQueryService;
import com.zhevlakov.findjobtelegrambot.vacancy.playwrightscrapper.HhPlaywrightScrapper;
import com.zhevlakov.findjobtelegrambot.vacancy.provider.HhVacancyProvider;
import com.zhevlakov.findjobtelegrambot.vacancy.provider.TestProvider;
import com.zhevlakov.findjobtelegrambot.vacancy.provider.remotive.RemotiveVacancyProvider;
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
    private final UserQueryService userQueryService;
    private final HhVacancyProvider hhVacancyProvider;
    private final RemotiveVacancyProvider remotiveVacancyProvider;

    public TestCommandHandler(
            TestProvider testProvider,
            HhQueryBuilder hhQueryBuilder,
            HhPlaywrightScrapper hhPlaywrightScrapper,
            UserQueryService userQueryService,
            HhVacancyProvider hhVacancyProvider,
            RemotiveVacancyProvider remotiveVacancyProvider
    ) {
        this.testProvider = testProvider;
        this.hhQueryBuilder = hhQueryBuilder;
        this.hhPlaywrightScrapper = hhPlaywrightScrapper;
        this.userQueryService = userQueryService;
        this.hhVacancyProvider = hhVacancyProvider;
        this.remotiveVacancyProvider = remotiveVacancyProvider;
    }

    @Override
    public List<BotResponse> handle(Message message) {
        var userId = message.chat().id();


        return BotResponse.asList(BotResponse.post(userId, "Тест"));
//        return testProvider.fetchVacancies(userId);

//        log.info("Vacancies {}", hhPlaywrightScrapper.fetchVacancies());


    }

    @Override
    public CommandHandlerName getCommandHandlerName() {
        return CommandHandlerName.TEST;
    }
}
