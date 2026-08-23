package com.zhevlakov.findjobtelegrambot.callback.code.userquery;

public enum ExperienceCode {
    NO_EXP("Без опыта" ,"0"),
    ONE_TO_THREE("1-3 года", "1_3"),
    THREE_TO_SIX("3-6 лет", "3_6"),
    SIX_AND_MORE("Более 6 лет", "6_more");

    private final String buttonText;
    private final String expCode;

    ExperienceCode(
            String buttonText,
            String expCode
    ) {
        this.buttonText = buttonText;
        this.expCode = expCode;
    }

    public String getButtonText() {
        return buttonText;
    }

    public String getExpCode() {
        return expCode;
    }
}
