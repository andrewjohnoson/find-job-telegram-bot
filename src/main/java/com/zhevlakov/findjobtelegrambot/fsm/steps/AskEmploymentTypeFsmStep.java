package com.zhevlakov.findjobtelegrambot.fsm.steps;

import com.zhevlakov.findjobtelegrambot.callback.code.EmploymentTypeCode;
import com.zhevlakov.findjobtelegrambot.callback.code.QueryCode;
import com.zhevlakov.findjobtelegrambot.fsm.*;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardButtonContent;
import com.zhevlakov.findjobtelegrambot.user.query.UserQuery;
import com.zhevlakov.findjobtelegrambot.user.query.UserQueryService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class AskEmploymentTypeFsmStep implements FsmStep {
    private final UserQueryService userQueryService;

    public AskEmploymentTypeFsmStep(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @Override
    public String responseMessage() {
        return "Выберите тип занятости";
    }

    @Override
    public void setProperty(
            UserQuery query,
            String input
    ) {
        if (!query.getEmploymentTypeList().contains(input)) {
            query.addEmploymentType(input);
        } else {
            query.removeEmploymentType(input);
        }
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
        return List.of(
                new KeyboardButtonContent(EmploymentTypeCode.FULL.getButtonText(), EmploymentTypeCode.FULL.getExpCode()),
                new KeyboardButtonContent(EmploymentTypeCode.PART.getButtonText(), EmploymentTypeCode.PART.getExpCode()),
                new KeyboardButtonContent(EmploymentTypeCode.TRAINEE.getButtonText(), EmploymentTypeCode.TRAINEE.getExpCode()),
                new KeyboardButtonContent(QueryCode.NEXT.getButtonText(), QueryCode.NEXT.getExpCode())
        );
    }

    @Override
    public FsmStateCode inlineDataCode() {
        return FsmStateCode.ASK_EMPLOYMENT;
    }

    @Override
    public List<KeyboardButtonContent> getFormattedButtons(List<KeyboardButtonContent> buttons, Long chatId) {
        var query = userQueryService.getByChatId(chatId);

        return buttons.stream()
                .map(button -> {
                    var newText = query.getEmploymentTypeList().contains(button.name()) ?
                            "✅" + button.name() : button.name();
                    return new KeyboardButtonContent(newText, button.code()) ;
                })
                .toList();
    }
}
