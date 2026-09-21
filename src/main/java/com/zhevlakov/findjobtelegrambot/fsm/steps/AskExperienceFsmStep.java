package com.zhevlakov.findjobtelegrambot.fsm.steps;

import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;
import com.zhevlakov.findjobtelegrambot.callback.code.userquery.QueryCode;
import com.zhevlakov.findjobtelegrambot.fsm.*;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardButtonContent;
import com.zhevlakov.findjobtelegrambot.user.query.UserQuery;
import com.zhevlakov.findjobtelegrambot.user.query.UserQueryService;
import com.zhevlakov.findjobtelegrambot.vacancy.query.converter.Experience;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
public class AskExperienceFsmStep implements FsmStep {
    private final UserQueryService userQueryService;

    public AskExperienceFsmStep(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @Override
    public String responseMessage() {
        return "Выберите опыт работы (можно несколько)";
    }

    @Override
    public void setProperty(
            UserQuery query,
            String input
    ) {

        if (QueryCode.NEXT.getExpCode().equals(input)) {
            return;
        }

        Experience selectedExp = Experience.valueOf(input);

        if (!query.getExperienceList().contains(selectedExp)) {
            query.addExperience(selectedExp);
        } else {
            query.removeExperience(selectedExp);
        }
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
        List<KeyboardButtonContent> buttons = new ArrayList<>();

        for (Experience exp : Experience.values()) {
            buttons.add(KeyboardButtonContent.standardButton(exp.getUiText(), exp.name()));
        }

        buttons.add(KeyboardButtonContent.standardButton(
                QueryCode.NEXT.getButtonText(),
                QueryCode.NEXT.getExpCode()
        ));

        return buttons;
    }

    @Override
    public InlineDataCode inlineDataCode() {
        return InlineDataCode.ASK_EXPERIENCE;
    }

    @Override
    public List<KeyboardButtonContent> getFormattedButtons(List<KeyboardButtonContent> buttons, Long chatId) {
        var query = userQueryService.getByChatId(chatId);

        return buttons.stream()
                .map(button -> {
                    if (button.code().equals(QueryCode.NEXT.getExpCode())) {
                        return button;
                    }

                    Experience exp = Experience.valueOf(button.code());

                    var newText = query.getExperienceList().contains(exp) ?
                            "✅" + button.name() : button.name();
                    return KeyboardButtonContent.standardButton(newText, button.code()) ;
                })
                .toList();
    }
}
