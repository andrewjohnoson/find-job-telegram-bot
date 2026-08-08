package com.zhevlakov.findjobtelegrambot.fsm.steps;

import com.zhevlakov.findjobtelegrambot.fsm.*;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardButtonContent;
import com.zhevlakov.findjobtelegrambot.user.query.UserQuery;
import com.zhevlakov.findjobtelegrambot.user.query.UserQueryValidator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class AskCityFsmStep implements FsmStep {
    private final UserQueryValidator queryValidator;

    public AskCityFsmStep(UserQueryValidator queryValidator) {
        this.queryValidator = queryValidator;
    }

    @Override
    public String nextResponseMessage() {
        return "Выберите формат работы";
    }

    @Override
    public void setProperty(
            UserQuery query,
            String input
    ) {
        query.setCity(input);
    }

    @Override
    public FsmStates currentState() {
        return FsmStates.ASK_CITY;
    }

    @Override
    public FsmStates nextState() {
        return FsmStates.ASK_WORK_FORMAT;
    }

    @Override
    public Predicate<String> validator() {
        return queryValidator::isCity;
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
