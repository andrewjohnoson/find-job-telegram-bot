package com.zhevlakov.findjobtelegrambot.command;

import com.pengrad.telegrambot.model.Message;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CommandDispatcher {
    private final Map<String, CommandHandler> commandHandlersMap;
    private final UserService userService;

    @Autowired
    public CommandDispatcher(
            List<CommandHandler> commandHandlers,
            UserService userService
    ) {
        this.commandHandlersMap = commandHandlers.stream()
                .collect(Collectors.toMap(
                        commandHandler -> commandHandler.getCommandHandlerName().commandName(),
                        Function.identity(),
                        (existing, replacement) -> existing,
                        HashMap::new
                ));
        this.userService = userService;
    }

    public List<BotResponse> processCommand(Message message) {
        var chatId = message.chat().id();
        var commandValue = message.text();

        if (!userService.haveUser(chatId)
                && !commandValue.equals(CommandHandlerName.START.commandName())) {
            return BotResponse.asList(BotResponse.error(chatId, "Данная операция в данный момент не доступна."));
        }

        var commandHandler = commandHandlersMap.get(commandValue);
        if (commandHandler == null) {
            throw new IllegalArgumentException("Такой комманды нет в списке.");
        }

        return commandHandler.handle(message);
    }

    public boolean isCommand(Message message) {
        return message.text() != null && commandHandlersMap.containsKey(message.text());
    }
}
