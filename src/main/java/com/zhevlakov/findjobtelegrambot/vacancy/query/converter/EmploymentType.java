package com.zhevlakov.findjobtelegrambot.vacancy.query.converter;

public enum EmploymentType {
    FULL("Полная занятость"),
    PART("Частичная занятость"),
    PROBATION("Стажировка");

    private final String uiText;

    EmploymentType(String uiText) {
        this.uiText = uiText;
    }

    public String getUiText() {
        return uiText;
    }
}
