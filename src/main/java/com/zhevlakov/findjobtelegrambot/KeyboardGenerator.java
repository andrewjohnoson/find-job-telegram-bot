package com.zhevlakov.findjobtelegrambot;

import com.pengrad.telegrambot.model.request.*;
import com.zhevlakov.findjobtelegrambot.fsm.FsmStep;
import org.springframework.stereotype.Component;

@Component
public class KeyboardGenerator {
    public Keyboard getStartCommandKeyboard() {
        KeyboardButton[] buttons = {
                new KeyboardButton("Новый запрос"),
                new KeyboardButton("Избранное"),
                new KeyboardButton("Перестать искать")
        };

        return new ReplyKeyboardMarkup(buttons).resizeKeyboard(true);
    }

    public Keyboard getKeepPrevStateKeyboard(String data) {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton("Оставить прежним").callbackData("keepPrev_" + data));
    }

    public InlineKeyboardMarkup buildInlineKeyboard(FsmStep step) {

    }
}
