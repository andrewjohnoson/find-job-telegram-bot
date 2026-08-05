package com.zhevlakov.findjobtelegrambot.command;

import lombok.Getter;

@Getter
public enum CommandHandlerName {
    START("/start"),
    NEW_QUERY("Новый запрос"),
    FAVOURITE("Избранное"),
    STOP_NOTIFICATIONS("Перестать искать");

    private final String commandName;

    CommandHandlerName(String commandName) {
        this.commandName = commandName;
    }
}
