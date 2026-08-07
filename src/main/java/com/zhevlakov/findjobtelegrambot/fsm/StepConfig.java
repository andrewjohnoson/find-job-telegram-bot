package com.zhevlakov.findjobtelegrambot.fsm;

import java.util.function.Predicate;

public class StepConfig {
    private FsmStates nextState;
    private String nextStateMessage;
    private Predicate<String> validation;

    public StepConfig(
            FsmStates nextState,
            String nextStateMessage,
            Predicate<String> validation
    ) {
        this.nextState = nextState;
        this.nextStateMessage = nextStateMessage;
        this.validation = validation;
    }

    public String getNextStateMessage() {
        return nextStateMessage;
    }

    public void setNextStateMessage(String nextStateMessage) {
        this.nextStateMessage = nextStateMessage;
    }

    public FsmStates getNextState() {
        return nextState;
    }

    public void setNextState(FsmStates nextState) {
        this.nextState = nextState;
    }

    public Predicate<String> getValidation() {
        return validation;
    }

    public void setValidation(Predicate<String> validation) {
        this.validation = validation;
    }
}
