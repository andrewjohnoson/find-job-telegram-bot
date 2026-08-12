package com.zhevlakov.findjobtelegrambot.callback.code;

public enum WorkFormatCode {
    IN_PERSON("На месте работодателя", "in_person"),
    REMOTE("Удалённо", "remote"),
    HYBRID("Гибрид", "hybrid");

    private final String buttonText;
    private final String expCode;

    WorkFormatCode(
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
