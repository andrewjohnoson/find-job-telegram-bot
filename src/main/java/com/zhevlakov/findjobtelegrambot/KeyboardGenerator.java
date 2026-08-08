package com.zhevlakov.findjobtelegrambot;

import com.pengrad.telegrambot.model.request.*;
import com.zhevlakov.findjobtelegrambot.fsm.FsmStep;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

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
                new InlineKeyboardButton("Продолжить").callbackData("keepPrev:" + data));
    }

    public InlineKeyboardMarkup buildInlineKeyboard(FsmStep step) {
        InlineKeyboardButton[] buttons = (InlineKeyboardButton[]) step.keyboardButtonsNames().stream()
                .map(content ->
                        new InlineKeyboardButton(content.name()).callbackData(
                            step.inlineDataCode().getInlineButtonCode() + ":" + content.code()
                        ))
                .toArray();
        return new InlineKeyboardMarkup(buttons);
    }
}
