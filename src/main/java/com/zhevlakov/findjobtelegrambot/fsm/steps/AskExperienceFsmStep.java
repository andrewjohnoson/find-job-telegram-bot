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
    public List<KeyboardButtonContent> keyboardButtonsNames() {
        return List.of(
                new KeyboardButtonContent("Без опыта", ButtonCode.FIRST_BUTTON),
                new KeyboardButtonContent("1-3 года", ButtonCode.SECOND_BUTTON),
                new KeyboardButtonContent("3-6 лет", ButtonCode.THIRD_BUTTON),
                new KeyboardButtonContent("Более 6 лет", ButtonCode.FOURTH_BUTTON),
                new KeyboardButtonContent("Продолжить", ButtonCode.FIFTH_BUTTON)
        );
    }
}
