package com.zhevlakov.findjobtelegrambot.fsm.steps;

import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;
import com.zhevlakov.findjobtelegrambot.callback.code.userquery.QueryCode;
import com.zhevlakov.findjobtelegrambot.callback.code.userquery.WorkFormatCode;
import com.zhevlakov.findjobtelegrambot.fsm.*;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardButtonContent;
import com.zhevlakov.findjobtelegrambot.user.query.UserQuery;
import com.zhevlakov.findjobtelegrambot.user.query.UserQueryService;
import org.springframework.stereotype.Component;

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
        if (!query.getWorkFormatList().contains(input)) {
            query.addWorkFormat(input);
        } else {
            query.removeWorkFormat(input);
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
        return List.of(
                KeyboardButtonContent.standardButton(WorkFormatCode.IN_PERSON.getButtonText(), WorkFormatCode.IN_PERSON.getExpCode()),
                KeyboardButtonContent.standardButton(WorkFormatCode.REMOTE.getButtonText(), WorkFormatCode.IN_PERSON.getExpCode()),
                KeyboardButtonContent.standardButton(WorkFormatCode.HYBRID.getButtonText(), WorkFormatCode.HYBRID.getExpCode()),
                KeyboardButtonContent.standardButton(QueryCode.NEXT.getButtonText(), QueryCode.NEXT.getExpCode())
        );
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
                    var newText = query.getWorkFormatList().contains(button.name()) ?
                            "✅" + button.name() : button.name();
                    return KeyboardButtonContent.standardButton(newText, button.code()) ;
                })
                .toList();
    }
}
