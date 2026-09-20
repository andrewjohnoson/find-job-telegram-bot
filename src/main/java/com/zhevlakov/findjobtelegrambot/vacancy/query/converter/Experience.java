package com.zhevlakov.findjobtelegrambot.vacancy.query.converter;

public enum Experience {
    NO_EXPERIENCE("Без опыта"),
    FROM_1_TO_3("1-3 года"),
    FROM_3_TO_6("3-6 лет"),
    MORE_THAN_6("Более 6 лет");

    private final String uiText;

    Experience(String uiText) {
        this.uiText = uiText;
    }

    public String getUiText() {
        return uiText;
    }
}
