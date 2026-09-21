package com.zhevlakov.findjobtelegrambot.fsm.steps;

import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;
import com.zhevlakov.findjobtelegrambot.callback.code.userquery.QueryCode;
import com.zhevlakov.findjobtelegrambot.fsm.*;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardButtonContent;
import com.zhevlakov.findjobtelegrambot.user.query.UserQuery;
import com.zhevlakov.findjobtelegrambot.user.query.UserQueryService;
import com.zhevlakov.findjobtelegrambot.vacancy.query.converter.EmploymentType;
import com.zhevlakov.findjobtelegrambot.vacancy.query.converter.WorkFormat;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Component
public class AskWorkFormatFsmStep implements FsmStep {
    private final UserQueryService userQueryService;

    public AskWorkFormatFsmStep(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @Override
    public String responseMessage() {
        return "Выберите формат работы";
    }

    @Override
    public void setProperty(
            UserQuery query,
            String input
    ) {
        if (QueryCode.NEXT.getExpCode().equals(input)) {
            return;
        }

        WorkFormat selectedWorkForm = WorkFormat.valueOf(input);

        if (!query.getWorkFormatList().contains(selectedWorkForm)) {
            query.addWorkFormat(selectedWorkForm);
        } else {
            query.removeWorkFormat(selectedWorkForm);
        }
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
        List<KeyboardButtonContent> buttons = new ArrayList<>();

        for (WorkFormat workFmt : WorkFormat.values()) {
            buttons.add(KeyboardButtonContent.standardButton(workFmt.getUiText(), workFmt.name()));
        }

        buttons.add(KeyboardButtonContent.standardButton(
                QueryCode.NEXT.getButtonText(),
                QueryCode.NEXT.getExpCode()
        ));

        return buttons;
    }

    @Override
    public InlineDataCode inlineDataCode() {
        return InlineDataCode.ASK_WORK_FORMAT;
    }

    @Override
    public List<KeyboardButtonContent> getFormattedButtons(List<KeyboardButtonContent> buttons, Long chatId) {
        var query = userQueryService.getByChatId(chatId);

        return buttons.stream()
                .map(button -> {
                    if (button.code().equals(QueryCode.NEXT.getExpCode())) {
                        return button;
                    }

                    WorkFormat workFmt = WorkFormat.valueOf(button.code());

                    var newText = query.getWorkFormatList().contains(workFmt) ?
                            "✅" + button.name() : button.name();
                    return KeyboardButtonContent.standardButton(newText, button.code()) ;
                })
                .toList();
    }
}
