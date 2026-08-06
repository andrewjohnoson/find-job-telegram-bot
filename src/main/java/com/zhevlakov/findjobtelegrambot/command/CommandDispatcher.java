package com.zhevlakov.findjobtelegrambot.command;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
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
                        commandHandler -> commandHandler.getCommandHandlerName().getCommandName(),
                        Function.identity(),
                        (existing, replacement) -> existing,
                        HashMap::new
                ));
        this.userService = userService;
    }

    public AbstractSendRequest<?> processCommand(Message message) {
        var chatId = message.chat().id();
        var commandValue = message.text();

        if (!userService.haveUser(chatId)
                && !commandValue.equals(CommandHandlerName.START.getCommandName())) {
            return new SendMessage(chatId, "Данная операция в данный момент не доступна.");
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
