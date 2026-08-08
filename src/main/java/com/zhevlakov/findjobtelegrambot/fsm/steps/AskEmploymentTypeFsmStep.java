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
        return "Ввод запроса завершён.";
    }

    @Override
    public void setProperty(
            UserQuery query,
            String input
    ) {
        query.setEmploymentType(input);
    }

    @Override
    public FsmStates currentState() {
        return FsmStates.ASK_EMPLOYMENT_TYPE;
    }

    @Override
    public FsmStates nextState() {
        return FsmStates.FREE;
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
        return FsmStateCode.ASK_EMPLOYMENT;
    }
}
