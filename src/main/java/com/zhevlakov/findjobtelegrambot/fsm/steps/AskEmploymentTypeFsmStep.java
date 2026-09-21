package com.zhevlakov.findjobtelegrambot.fsm.steps;

import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;
import com.zhevlakov.findjobtelegrambot.callback.code.userquery.QueryCode;
import com.zhevlakov.findjobtelegrambot.fsm.*;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardButtonContent;
import com.zhevlakov.findjobtelegrambot.user.query.UserQuery;
import com.zhevlakov.findjobtelegrambot.user.query.UserQueryService;
import com.zhevlakov.findjobtelegrambot.vacancy.query.converter.EmploymentType;
import com.zhevlakov.findjobtelegrambot.vacancy.query.converter.Experience;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
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
        if (QueryCode.NEXT.getExpCode().equals(input)) {
            return;
        }

        EmploymentType selectedEmplType = EmploymentType.valueOf(input);

        if (!query.getEmploymentTypeList().contains(selectedEmplType)) {
            query.addEmploymentType(selectedEmplType);
        } else {
            query.removeEmploymentType(selectedEmplType);
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
        List<KeyboardButtonContent> buttons = new ArrayList<>();

        for (EmploymentType empl : EmploymentType.values()) {
            buttons.add(KeyboardButtonContent.standardButton(empl.getUiText(), empl.name()));
        }

        buttons.add(KeyboardButtonContent.standardButton(
                QueryCode.NEXT.getButtonText(),
                QueryCode.NEXT.getExpCode()
        ));

        return buttons;
    }

    @Override
    public InlineDataCode inlineDataCode() {
        return InlineDataCode.ASK_EMPLOYMENT;
    }

    @Override
    public List<KeyboardButtonContent> getFormattedButtons(List<KeyboardButtonContent> buttons, Long chatId) {
        var query = userQueryService.getByChatId(chatId);

        return buttons.stream()
                .map(button -> {
                    if (button.code().equals(QueryCode.NEXT.getExpCode())) {
                        return button;
                    }

                    EmploymentType empl = EmploymentType.valueOf(button.code());

                    var newText = query.getEmploymentTypeList().contains(empl) ?
                            "✅" + button.name() : button.name();
                    return KeyboardButtonContent.standardButton(newText, button.code()) ;
                })
                .toList();
    }
}
