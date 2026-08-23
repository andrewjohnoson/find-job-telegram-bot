package com.zhevlakov.findjobtelegrambot.callback.code;

public enum InlineDataCode {
    ASK_EMPLOYMENT("empl"),
    ASK_EXPERIENCE("exp"),
    ASK_WORK_FORMAT("wft"),
    NEXT("next"),

    // vacancy buttons
    VACANCY("vac"),
    VACANCY_CLICK("cl"),
    VACANCY_FAVOURITE("fav"),
    VACANCY_HIDE("hid");

    private final String inlineButtonCode;

    InlineDataCode(String inlineButtonCode) {
        this.inlineButtonCode = inlineButtonCode;
    }

    public String getInlineButtonCode() {
        return inlineButtonCode;
    }
}
