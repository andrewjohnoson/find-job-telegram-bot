package com.zhevlakov.findjobtelegrambot.callback.code;

public enum InlineDataCode {
    ASK_EMPLOYMENT("empl", null),
    ASK_EXPERIENCE("exp", null),
    ASK_WORK_FORMAT("wft", null),
    NEXT("next", null),

    // vacancy buttons
    VACANCY_CLICK("vac_cl", "Перейти"),
    VACANCY_FAVOURITE("vac_fav", "В избранное"),
    VACANCY_NOT_FAVOURITE("vac_nfav", "Убрать из избарнных"),
    VACANCY_HIDE("vac_hid", "Скрыть"),
    VACANCY_UNCOVER("vac_uncov", "Показать"),
    VACANCY_LOAD_MORE("load_more", "Показать ещё ⬇️");

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
