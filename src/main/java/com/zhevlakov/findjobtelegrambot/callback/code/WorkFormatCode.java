package com.zhevlakov.findjobtelegrambot.callback.code;

public enum WorkFormatCode {
    IN_PERSON("На месте работодателя", "in_person"),
    REMOTE("Удалённо", "remote"),
    HYBRID("Гибрид", "hybrid");

    private final String expCode;
    private final String buttonText;

    WorkFormatCode(
            String expCode,
            String buttonText
    ) {
        this.expCode = expCode;
        this.buttonText = buttonText;
    }

    public String getButtonText() {
        return buttonText;
    }

    public String getExpCode() {
        return expCode;
    }
}
