package com.zhevlakov.findjobtelegrambot.callback.code;

public enum QueryCode {
    NEXT("Продолжить", "next");

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
