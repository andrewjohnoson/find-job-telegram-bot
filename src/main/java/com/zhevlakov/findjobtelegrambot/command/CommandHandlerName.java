package com.zhevlakov.findjobtelegrambot.command;

public enum CommandHandlerName {
    START("/start");

    private final String commandName;

    CommandHandlerName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
