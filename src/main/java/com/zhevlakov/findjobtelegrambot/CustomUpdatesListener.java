package com.zhevlakov.findjobtelegrambot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomUpdatesListener implements UpdatesListener {

    private final UpdateHandler updateHandler;

    public CustomUpdatesListener(UpdateHandler updateHandler) {
        this.updateHandler = updateHandler;
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(updateHandler::handleUpdate);
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
