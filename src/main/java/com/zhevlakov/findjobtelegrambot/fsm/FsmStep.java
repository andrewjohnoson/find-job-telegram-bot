package com.zhevlakov.findjobtelegrambot.fsm;

import com.zhevlakov.findjobtelegrambot.user.query.UserQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public interface FsmStep {
    String nextResponseMessage();
    void setProperty(UserQuery query, String input);
    FsmStates currentState();
    FsmStates nextState();
    InputType inputType();
    Predicate<String> validator();
    List<KeyboardButtonContent> keyboardButtonsNames();
    FsmStateCode inlineDataCode();
}
