package com.zhevlakov.findjobtelegrambot.fsm.steps;

import com.zhevlakov.findjobtelegrambot.callback.code.ExperienceCode;
import com.zhevlakov.findjobtelegrambot.callback.code.QueryCode;
import com.zhevlakov.findjobtelegrambot.fsm.*;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardButtonContent;
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
                new KeyboardButtonContent(ExperienceCode.NO_EXP.getButtonText(), ExperienceCode.NO_EXP.getExpCode()),
                new KeyboardButtonContent(ExperienceCode.ONE_TO_THREE.getButtonText(), ExperienceCode.ONE_TO_THREE.getExpCode()),
                new KeyboardButtonContent(ExperienceCode.THREE_TO_SIX.getButtonText(), ExperienceCode.THREE_TO_SIX.getExpCode()),
                new KeyboardButtonContent(ExperienceCode.SIX_AND_MORE.getButtonText(), ExperienceCode.SIX_AND_MORE.getExpCode()),
                new KeyboardButtonContent(QueryCode.NEXT.getButtonText(), QueryCode.NEXT.getExpCode())
        );
    }

    @Override
    public FsmStateCode inlineDataCode() {
        return FsmStateCode.ASK_EXPERIENCE;
    }
}
