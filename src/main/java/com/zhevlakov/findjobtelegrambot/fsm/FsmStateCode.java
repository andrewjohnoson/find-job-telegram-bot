package com.zhevlakov.findjobtelegrambot.fsm;

public enum FsmStateCode {
    ASK_EMPLOYMENT("empl"),
    ASK_EXPERIENCE("exp"),
    ASK_WORK_FORMAT("wft"),
    NEXT("next");

    private final String inlineButtonCode;

    FsmStateCode(String inlineButtonCode) {
        this.inlineButtonCode = inlineButtonCode;
    }

    public String getInlineButtonCode() {
        return inlineButtonCode;
    }
}
