package com.zhevlakov.findjobtelegrambot.fsm.steps;

import com.zhevlakov.findjobtelegrambot.fsm.*;
import com.zhevlakov.findjobtelegrambot.user.query.UserQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class AskWorkFormatFsmStep implements FsmStep {

    @Override
    public String nextResponseMessage() {
        return "Введите желаемую з/п:";
    }

    @Override
    public void setProperty(
            UserQuery query,
            String input
    ) {
        query.setWorkFormat(input);
    }

    @Override
    public FsmStates currentState() {
        return FsmStates.ASK_WORK_FORMAT;
    }

    @Override
    public FsmStates nextState() {
        return FsmStates.ASK_SALARY;
    }

    @Override
    public Predicate<String> validator() {
        return null;
    }

    @Override
    public InputType inputType() {
        return InputType.INLINE_CHOICE;
    }

    @Override
    public List<KeyboardButtonContent> nextKeyboardButtons() {
        return null;
    }

    @Override
    public FsmStateCode inlineDataCode() {
        return FsmStateCode.ASK_WORK_FORMAT;
    }
}
