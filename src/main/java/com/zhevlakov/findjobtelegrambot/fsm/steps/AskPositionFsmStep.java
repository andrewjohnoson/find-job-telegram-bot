package com.zhevlakov.findjobtelegrambot.fsm.steps;

import com.zhevlakov.findjobtelegrambot.fsm.*;
import com.zhevlakov.findjobtelegrambot.user.query.UserQuery;
import com.zhevlakov.findjobtelegrambot.user.query.UserQueryValidator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class AskPositionFsmStep implements FsmStep {
    private final UserQueryValidator queryValidator;

    public AskPositionFsmStep(UserQueryValidator queryValidator) {
        this.queryValidator = queryValidator;
    }

    @Override
    public String nextResponseMessage() {
        return "Выберите опыт работы";
    }

    @Override
    public void setProperty(
            UserQuery query,
            String input
    ) {
        query.setPosition(input);
    }

    @Override
    public FsmStates currentState() {
        return FsmStates.ASK_POSITION;
    }

    @Override
    public FsmStates nextState() {
        return FsmStates.ASK_EXPERIENCE;
    }

    @Override
    public Predicate<String> validator() {
        return queryValidator::isPosition;
    }

    @Override
    public InputType inputType() {
        return InputType.USUAL_TEXT;
    }

    @Override
    public List<KeyboardButtonContent> nextKeyboardButtons() {
        return List.of(
                new KeyboardButtonContent("Без опыта", "0"),
                new KeyboardButtonContent("1-3 года", "1_3"),
                new KeyboardButtonContent("3-6 лет", "3_6"),
                new KeyboardButtonContent("Более 6 лет", "6_more"),
                new KeyboardButtonContent("Продолжить", "next")
        );
    }

    @Override
    public FsmStateCode inlineDataCode() {
        return null;
    }
}
