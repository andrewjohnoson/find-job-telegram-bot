package com.zhevlakov.findjobtelegrambot.keyboard.provider;

import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardButtonContent;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardProvider;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardSettings;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VacancyKeyboardProvider implements KeyboardProvider {
    private List<KeyboardButtonContent> buttonList;

    public void setButtonList(List<KeyboardButtonContent> buttonList) {
        this.buttonList = buttonList;
    }

    @Override
    public KeyboardSettings getKeyboardSettings(Long chatId) {

        return new KeyboardSettings(
                buttonList,
                null,
                InlineDataCode.VACANCY.inlineButtonCode()
        );
    }
}
