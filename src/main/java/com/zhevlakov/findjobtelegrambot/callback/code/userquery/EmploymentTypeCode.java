package com.zhevlakov.findjobtelegrambot.callback.code.userquery;

public enum EmploymentTypeCode {
    FULL("Полная занятость", "full"),
    PART("Частичная занятость", "part"),
    TRAINEE("Стажировка", "train");

    private final String buttonText;
    private final String emplCode;

    EmploymentTypeCode(
            String buttonText,
            String emplCode
    ) {
        this.buttonText = buttonText;
        this.emplCode = emplCode;
    }

    public String getButtonText() {
        return buttonText;
    }

    public String getEmplCode() {
        return emplCode;
    }
}
