package com.zhevlakov.findjobtelegrambot.callback.code;

public enum InlineDataCode {
    ASK_EMPLOYMENT("empl", null),
    ASK_EXPERIENCE("exp", null),
    ASK_WORK_FORMAT("wft", null),
    NEXT("next", null),

    // vacancy buttons
    VACANCY("vac", null),
    VACANCY_CLICK("cl", "Перейти"),
    VACANCY_FAVOURITE("fav", "В избранное"),
    VACANCY_NOT_FAVOURITE("nfav", "Убрать из избарнных"),
    VACANCY_HIDE("hid", "Скрыть"),
    VACANCY_UNCOVER("uncov", "Показать");

    private final String inlineButtonCode;
    private final String buttonText;

    InlineDataCode(
            String inlineButtonCode,
            String buttonText
    ) {
        this.inlineButtonCode = inlineButtonCode;
        this.buttonText = buttonText;
    }

    public String inlineButtonCode() {
        return inlineButtonCode;
    }

    public String buttonText() {
        return buttonText;
    }
}
