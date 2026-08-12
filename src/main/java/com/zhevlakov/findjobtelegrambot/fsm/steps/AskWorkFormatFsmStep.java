package com.zhevlakov.findjobtelegrambot.fsm.steps;

import com.zhevlakov.findjobtelegrambot.callback.code.QueryCode;
import com.zhevlakov.findjobtelegrambot.callback.code.WorkFormatCode;
import com.zhevlakov.findjobtelegrambot.fsm.*;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardButtonContent;
import com.zhevlakov.findjobtelegrambot.user.query.UserQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class AskWorkFormatFsmStep implements FsmStep {

    @Override
    public String responseMessage() {
        return "Выберите формат работы";
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
        return List.of(
                new KeyboardButtonContent(WorkFormatCode.IN_PERSON.getButtonText(), WorkFormatCode.IN_PERSON.getExpCode()),
                new KeyboardButtonContent(WorkFormatCode.REMOTE.getButtonText(), WorkFormatCode.IN_PERSON.getExpCode()),
                new KeyboardButtonContent(WorkFormatCode.HYBRID.getButtonText(), WorkFormatCode.HYBRID.getExpCode()),
                new KeyboardButtonContent(QueryCode.NEXT.getButtonText(), QueryCode.NEXT.getExpCode())
        );
    }

    @Override
    public FsmStateCode inlineDataCode() {
        return FsmStateCode.ASK_WORK_FORMAT;
    }
}
