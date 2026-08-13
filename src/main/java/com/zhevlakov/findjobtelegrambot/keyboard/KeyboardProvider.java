package com.zhevlakov.findjobtelegrambot.keyboard;

public interface KeyboardProvider {
    KeyboardSettings getKeyboardSettings(Long chatId);
}
