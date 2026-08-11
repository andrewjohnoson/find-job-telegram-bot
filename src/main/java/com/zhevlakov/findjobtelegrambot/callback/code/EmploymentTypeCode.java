package com.zhevlakov.findjobtelegrambot.callback.code;

public enum EmploymentTypeCode {
    FULL("Полная занятость", "full"),
    PART("Частичная занятость", "part"),
    TRAINEE("Стажировка", "train");

    private final String buttonText;
    private final String expCode;

    EmploymentTypeCode(
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
