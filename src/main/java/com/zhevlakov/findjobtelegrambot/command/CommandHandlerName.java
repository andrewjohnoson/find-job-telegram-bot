package com.zhevlakov.findjobtelegrambot.command;

public enum CommandHandlerName {
    START("/start"),
    CANCEL("/cancel"),
    NEW_QUERY("Новый запрос"),
    FAVOURITE("Избранное"),
    STOP_NOTIFICATIONS("Перестать искать");

    private final String commandName;

    CommandHandlerName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
