package com.zhevlakov.findjobtelegrambot.keyboard;

public record KeyboardButtonContent(
        String name,
        String code,
        String url
) {
    public static KeyboardButtonContent standardButton(String name, String code) {
        return new KeyboardButtonContent(name, code, null);
    }

    public static KeyboardButtonContent urlButton(String name, String code, String url) {
        return new KeyboardButtonContent(name, code, url);
    }
}
