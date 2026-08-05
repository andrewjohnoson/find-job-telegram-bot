package com.zhevlakov.findjobtelegrambot.command;

import com.pengrad.telegrambot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CommandDispatcher {
    private Map<String, CommandHandler> commandHandlersMap;

    @Autowired
    public CommandDispatcher(
            List<CommandHandler> commandHandlers
    ) {
        this.commandHandlersMap = commandHandlers.stream()
                .collect(Collectors.toMap(
                        commandHandler -> commandHandler.getCommandHandlerName().getCommandName(),
                        Function.identity(),
                        (existing, replacement) -> existing,
                        HashMap::new
                ));
    }

    public void processCommand(Message message) {
        var commandValue = message.text();
        var commandHandler = commandHandlersMap.get(commandValue);
        if (commandHandler == null) {
            throw new IllegalArgumentException("Такой комманды нет в списке.");
        }
        commandHandler.handle(message);


    }
}
