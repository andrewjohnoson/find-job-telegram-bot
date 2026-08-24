package com.zhevlakov.findjobtelegrambot.fsm;

import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardButtonContent;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardProvider;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardSettings;
import com.zhevlakov.findjobtelegrambot.user.query.UserQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public interface FsmStep extends KeyboardProvider {
    String responseMessage();
    void setProperty(UserQuery query, String input);
    FsmStates currentState();
    FsmStates nextState();
    InputType inputType();
    Predicate<String> validator();
    List<KeyboardButtonContent> nextKeyboardButtons();
    InlineDataCode inlineDataCode();

    default List<KeyboardButtonContent> getFormattedButtons(List<KeyboardButtonContent> buttons, Long chatId) {
        return buttons;
    }

    @Override
    default KeyboardSettings getKeyboardSettings(Long chatId) {
        return new KeyboardSettings(
                this.nextKeyboardButtons(),
                (buttons) -> this.getFormattedButtons(buttons, chatId),
                this.inlineDataCode().inlineButtonCode()
        );
    }
}
