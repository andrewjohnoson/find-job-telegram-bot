package com.zhevlakov.findjobtelegrambot.fsm.steps;

import com.zhevlakov.findjobtelegrambot.fsm.*;
import com.zhevlakov.findjobtelegrambot.user.query.UserQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class AskExperienceFsmStep implements FsmStep {
    @Override
    public String nextResponseMessage() {
        return "Введите город:";
    }

    @Override
    public void setProperty(
            UserQuery query,
            String input
    ) {
        query.setExperience(input);
    }

    @Override
    public FsmStates currentState() {
        return FsmStates.ASK_EXPERIENCE;
    }

    @Override
    public FsmStates nextState() {
        return FsmStates.ASK_CITY;
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
        return List.of(
                new KeyboardButtonContent("На месте работодателя", "in_person"),
                new KeyboardButtonContent("Удалённо", "remote"),
                new KeyboardButtonContent("Гибрид", "hybrid"),
                new KeyboardButtonContent("Продолжить", "next")
        );
    }

    @Override
    public FsmStateCode inlineDataCode() {
        return FsmStateCode.ASK_EXPERIENCE;
    }
}
