package com.zhevlakov.findjobtelegrambot.fsm.steps;

import com.zhevlakov.findjobtelegrambot.fsm.*;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardButtonContent;
import com.zhevlakov.findjobtelegrambot.user.query.UserQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class AskSalaryFsmStep implements FsmStep {
    @Override
    public String responseMessage() {
        return "Введите желаемую з/п:";
    }

    @Override
    public void setProperty(
            UserQuery query,
            String input
    ) {
        query.setSalary(input);
    }

    @Override
    public FsmStates currentState() {
        return FsmStates.ASK_SALARY;
    }

    @Override
    public FsmStates nextState() {
        return FsmStates.ASK_EMPLOYMENT_TYPE;
    }

    @Override
    public Predicate<String> validator() {
        return null;
    }

    @Override
    public InputType inputType() {
        return InputType.USUAL_TEXT;
    }

    @Override
    public List<KeyboardButtonContent> nextKeyboardButtons() {
        return null;
    }

    @Override
    public FsmStateCode inlineDataCode() {
        return null;
    }
}
