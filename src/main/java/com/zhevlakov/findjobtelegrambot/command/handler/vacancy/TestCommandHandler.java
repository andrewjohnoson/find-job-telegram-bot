package com.zhevlakov.findjobtelegrambot.command.handler.vacancy;

import com.pengrad.telegrambot.model.Message;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.command.CommandHandler;
import com.zhevlakov.findjobtelegrambot.command.CommandHandlerName;
import com.zhevlakov.findjobtelegrambot.user.query.UserQueryService;
import com.zhevlakov.findjobtelegrambot.vacancy.Vacancy;
import com.zhevlakov.findjobtelegrambot.vacancy.VacancyDto;
import com.zhevlakov.findjobtelegrambot.vacancy.VacancyManager;
import com.zhevlakov.findjobtelegrambot.vacancy.provider.TestProvider;
import com.zhevlakov.findjobtelegrambot.vacancy.provider.arbeitnow.ArbeitnowVacancyProvider;
import com.zhevlakov.findjobtelegrambot.vacancy.provider.remotive.RemotiveVacancyProvider;
import com.zhevlakov.findjobtelegrambot.vacancy.ui.VacancyUiManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestCommandHandler implements CommandHandler {
    private final TestProvider testProvider;
    private final Logger log = LoggerFactory.getLogger(TestCommandHandler.class);
    private final UserQueryService userQueryService;
    private final RemotiveVacancyProvider remotiveVacancyProvider;
    private final VacancyUiManager uiManager;
    private final VacancyManager vacancyManager;
    private final ArbeitnowVacancyProvider arbeitnowVacancyProvider;

    public TestCommandHandler(
            TestProvider testProvider,
            UserQueryService userQueryService,
            RemotiveVacancyProvider remotiveVacancyProvider,
            VacancyUiManager uiManager,
            VacancyManager vacancyManager,
            ArbeitnowVacancyProvider arbeitnowVacancyProvider
    ) {
        this.testProvider = testProvider;
        this.userQueryService = userQueryService;
        this.remotiveVacancyProvider = remotiveVacancyProvider;
        this.uiManager = uiManager;
        this.vacancyManager = vacancyManager;
        this.arbeitnowVacancyProvider = arbeitnowVacancyProvider;
    }

    @Override
    public List<BotResponse> handle(Message message) {
        var userId = message.chat().id();
        List<VacancyDto> vacancyDtoList = arbeitnowVacancyProvider.fetch(userId);
        log.info("Полученные отфильтрованные вакансии: {}", vacancyDtoList);
        return BotResponse.asList(BotResponse.post(userId, "test"));
//        vacancyManager.fetch(userId);
//        return uiManager.outputVacancies(userId);
    }

    @Override
    public CommandHandlerName getCommandHandlerName() {
        return CommandHandlerName.TEST;
    }
}
