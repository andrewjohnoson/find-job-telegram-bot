package com.zhevlakov.findjobtelegrambot.fsm.steps;

import com.zhevlakov.findjobtelegrambot.callback.code.InlineDataCode;
import com.zhevlakov.findjobtelegrambot.callback.code.userquery.ExperienceCode;
import com.zhevlakov.findjobtelegrambot.callback.code.userquery.QueryCode;
import com.zhevlakov.findjobtelegrambot.fsm.*;
import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardButtonContent;
import com.zhevlakov.findjobtelegrambot.user.query.UserQuery;
import com.zhevlakov.findjobtelegrambot.user.query.UserQueryService;
import org.springframework.stereotype.Component;

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
        return "Выберите опыт работы";
    }

    @Override
    public void setProperty(
            UserQuery query,
            String input
    ) {
        if (!query.getExperienceList().contains(input)) {
            query.addExperience(input);
        } else {
            query.removeExperience(input);
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
        return List.of(
                KeyboardButtonContent.standardButton(ExperienceCode.NO_EXP.getButtonText(), ExperienceCode.NO_EXP.getExpCode()),
                KeyboardButtonContent.standardButton(ExperienceCode.ONE_TO_THREE.getButtonText(), ExperienceCode.ONE_TO_THREE.getExpCode()),
                KeyboardButtonContent.standardButton(ExperienceCode.THREE_TO_SIX.getButtonText(), ExperienceCode.THREE_TO_SIX.getExpCode()),
                KeyboardButtonContent.standardButton(ExperienceCode.SIX_AND_MORE.getButtonText(), ExperienceCode.SIX_AND_MORE.getExpCode()),
                KeyboardButtonContent.standardButton(QueryCode.NEXT.getButtonText(), QueryCode.NEXT.getExpCode())
        );
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
                    var newText = query.getExperienceList().contains(button.name()) ?
                            "✅" + button.name() : button.name();
                    return KeyboardButtonContent.standardButton(newText, button.code()) ;
                })
                .toList();
    }
}
