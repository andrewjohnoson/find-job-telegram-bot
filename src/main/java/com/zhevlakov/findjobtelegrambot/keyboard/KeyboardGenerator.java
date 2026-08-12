package com.zhevlakov.findjobtelegrambot.keyboard;

import com.pengrad.telegrambot.model.request.*;
import com.zhevlakov.findjobtelegrambot.callback.code.QueryCode;
import com.zhevlakov.findjobtelegrambot.fsm.FsmStep;
import org.springframework.stereotype.Component;

import java.util.List;

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
                new InlineKeyboardButton(QueryCode.NEXT.getButtonText())
                        .callbackData(QueryCode.NEXT.getExpCode() + ":" + data));
    }

    public InlineKeyboardMarkup buildInlineKeyboard(FsmStep step, Long chatId) {
        List<KeyboardButtonContent> stepButtons = step.nextKeyboardButtons();

        stepButtons = step.getFormattedButtons(stepButtons, chatId);

        InlineKeyboardButton[] buttons = stepButtons.stream()
                .map(content ->
                        new InlineKeyboardButton(content.name())
                                .callbackData(
                                        step.inlineDataCode().getInlineButtonCode() + ":" + content.code()
                                ))
                .toArray(InlineKeyboardButton[]::new);

        int buttonsPerRow = 3;
        int rowCount = (int) Math.ceil((double) buttons.length / buttonsPerRow);

        InlineKeyboardButton[][] keyboard = new InlineKeyboardButton[rowCount][];

        for (int i = 0; i < rowCount; i++) {
            int from = i * buttonsPerRow;
            int to = Math.min(from + buttonsPerRow, buttons.length);

            keyboard[i] = new InlineKeyboardButton[to - from];

            System.arraycopy(
                    buttons,
                    from,
                    keyboard[i],
                    0,
                    to - from
            );
        }

        return new InlineKeyboardMarkup(keyboard);
    }
}
