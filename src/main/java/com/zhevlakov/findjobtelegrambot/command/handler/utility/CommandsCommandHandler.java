package com.zhevlakov.findjobtelegrambot.command.handler.utility;

import com.pengrad.telegrambot.model.Message;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.command.CommandHandler;
import com.zhevlakov.findjobtelegrambot.command.CommandHandlerName;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

@Component
public class CommandsCommandHandler implements CommandHandler {
    @Override
    public List<BotResponse> handle(Message message) {
        var userId = message.chat().id();
        StringJoiner sj = new StringJoiner("\n");
        Arrays.stream(CommandHandlerName.values())
                .forEach(commandName -> {
                    if (commandName.commandName().startsWith("/")) {
                        sj.add(commandName.commandName());
                    }
                });
        return List.of(BotResponse.post(userId, sj.toString()));
    }

    @Override
    public CommandHandlerName getCommandHandlerName() {
        return CommandHandlerName.COMMANDS;
    }
}
