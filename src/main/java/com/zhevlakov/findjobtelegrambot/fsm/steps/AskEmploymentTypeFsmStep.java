package com.zhevlakov.findjobtelegrambot.fsm.steps;

import com.zhevlakov.findjobtelegrambot.fsm.*;
import com.zhevlakov.findjobtelegrambot.user.query.UserQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class AskEmploymentTypeFsmStep implements FsmStep {
    @Override
    public String nextResponseMessage() {
        return null;
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
        return FsmStates.ASK_EMPLOYMENT_TYPE;
    }

    @Override
    public FsmStates nextState() {
        return null;
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
    public List<KeyboardButtonContent> keyboardButtonsNames() {
        return List.of(
                new KeyboardButtonContent("Полная занятость", ButtonCode.FIRST_BUTTON),
                new KeyboardButtonContent("Частичная занятость", ButtonCode.SECOND_BUTTON),
                new KeyboardButtonContent("Стажировка", ButtonCode.THIRD_BUTTON),
                new KeyboardButtonContent("Продолжить", ButtonCode.FOURTH_BUTTON)
        );
    }

    @Override
    public FsmStateCode inlineDataCode() {
        return FsmStateCode.ASK_EMPLOYMENT;
    }
}
