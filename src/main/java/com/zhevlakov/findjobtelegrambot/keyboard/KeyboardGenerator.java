package com.zhevlakov.findjobtelegrambot.keyboard;

import com.pengrad.telegrambot.model.request.*;
import com.zhevlakov.findjobtelegrambot.callback.code.userquery.QueryCode;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KeyboardGenerator {
    public Keyboard getStartCommandKeyboard() {
        KeyboardButton[][] buttons = {
                new KeyboardButton[] {
                    new KeyboardButton("Новый запрос"),
                    new KeyboardButton("Показать текущий запрос"),
                },
                new KeyboardButton[]{
                        new KeyboardButton("Избранное"),
                        new KeyboardButton("Перестать искать")
                }
        };

        return new ReplyKeyboardMarkup(buttons).resizeKeyboard(true);
    }

    public Keyboard getKeepPrevStateKeyboard(String data) {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton(QueryCode.NEXT.getButtonText())
                        .callbackData(QueryCode.NEXT.getExpCode() + ":" + data));
    }

    public InlineKeyboardMarkup buildInlineKeyboard(List<KeyboardButtonContent> buttonList) {
        InlineKeyboardButton[] buttons = buttonList.stream()
                .map(content -> {
                    var newButton =  new InlineKeyboardButton(content.name())
                            .callbackData(content.code());
                    if (content.url() != null) {
                        newButton.url(content.url());
                    }
                    return newButton;
                })
                .toArray(InlineKeyboardButton[]::new);

        InlineKeyboardButton[][] keyboard = alignKeyboard(buttons);
        return new InlineKeyboardMarkup(keyboard);
    }

    public InlineKeyboardMarkup buildInlineKeyboard(KeyboardProvider provider, Long chatId) {
        var settings = provider.getKeyboardSettings(chatId);
        var finalButtons = settings.getFinalButtons();

        InlineKeyboardButton[] buttons = finalButtons.stream()
                .map(content -> {
                    var newButton =  new InlineKeyboardButton(content.name())
                            .callbackData(
                                    settings.inlineCode() + ":" + content.code()
                            );
                    if (content.url() != null) {
                        newButton.url(content.url());
                    }
                    return newButton;
                })
                .toArray(InlineKeyboardButton[]::new);

        InlineKeyboardButton[][] keyboard = alignKeyboard(buttons);

        return new InlineKeyboardMarkup(keyboard);
    }

    private InlineKeyboardButton[][] alignKeyboard(InlineKeyboardButton[] buttons) {
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
        return keyboard;
    }
}
