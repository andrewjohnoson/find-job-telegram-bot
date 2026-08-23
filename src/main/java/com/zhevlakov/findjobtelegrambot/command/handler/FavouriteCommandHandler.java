package com.zhevlakov.findjobtelegrambot.command.handler;

import com.pengrad.telegrambot.model.Message;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.command.CommandHandler;
import com.zhevlakov.findjobtelegrambot.command.CommandHandlerName;
import com.zhevlakov.findjobtelegrambot.vacancy.provider.HhVacancyProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FavouriteCommandHandler implements CommandHandler {
    private final Logger log = LoggerFactory.getLogger(FavouriteCommandHandler.class);

    private final HhVacancyProvider provider;

    public FavouriteCommandHandler(HhVacancyProvider provider) {
        this.provider = provider;
    }

    @Override
    public List<BotResponse> handle(Message message) {
        log.info(provider.fetchVacancies());
        return BotResponse.asList(BotResponse.post(message.chat().id(), "Выполнено"));
    }

    @Override
    public CommandHandlerName getCommandHandlerName() {
        return CommandHandlerName.FAVOURITE;
    }
}
