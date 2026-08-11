package com.zhevlakov.findjobtelegrambot.callback.code;

public enum QueryCode {
    NEXT("Более 6 лет", "6_more");

    private final String buttonText;
    private final String expCode;

    QueryCode(
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
