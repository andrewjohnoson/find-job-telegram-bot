package com.zhevlakov.findjobtelegrambot.keyboard;

import java.util.List;
import java.util.function.Function;

public record KeyboardSettings(
        List<KeyboardButtonContent> buttons,
        Function<List<KeyboardButtonContent>, List<KeyboardButtonContent>> formatter,
        String inlineCode
) {
    public List<KeyboardButtonContent> getFinalButtons() {
        return formatter != null ? formatter.apply(buttons) : buttons;
    }
}
