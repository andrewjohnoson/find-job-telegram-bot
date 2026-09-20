package com.zhevlakov.findjobtelegrambot.vacancy.query.converter;

public enum WorkFormat {
    ON_SITE("На месте работодателя"),
    REMOTE("Удалённо"),
    HYBRID("Гибрид");

    private final String uiText;

    WorkFormat(String uiText) {
        this.uiText = uiText;
    }

    public String getUiText() {
        return uiText;
    }
}
