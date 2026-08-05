package com.zhevlakov.findjobtelegrambot.bot;

import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomExceptionHandler implements ExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @Override
    public void onException(TelegramException e) {
        if (e.response() != null) {
            log.error("got error={}. it has description={}",
                    e.response().errorCode(), e.response().description());
        } else {
            e.printStackTrace();
        }
    }
}
