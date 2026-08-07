package com.zhevlakov.findjobtelegrambot.fsm;

import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.user.UserService;
import com.zhevlakov.findjobtelegrambot.user.query.UserQuery;
import com.zhevlakov.findjobtelegrambot.user.query.UserQueryValidator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class QueryFsmWizardService {
    private final Map<FsmStates, StepConfig> steps;
    private final UserQueryValidator validator;
    private final UserService userService;

    public QueryFsmWizardService(
            UserQueryValidator validator,
            UserService userService
    ) {
        this.validator = validator;
        this.steps = Map.of(
                FsmStates.ASK_POSITION, new StepConfig(FsmStates.ASK_CITY, "Выберите опыт работы", this.validator::isPosition),
                FsmStates.ASK_EXPERIENCE, new StepConfig(FsmStates.ASK_CITY, "Введите город:", null),
                FsmStates.ASK_CITY, new StepConfig(FsmStates.ASK_WORK_FORMAT, "Выберите формат работы", validator::isCity),
                FsmStates.ASK_WORK_FORMAT, new StepConfig(FsmStates.ASK_SALARY, "Введите желаемую з/п:", null),
                FsmStates.ASK_SALARY, new StepConfig(FsmStates.ASK_CITY, "Выберите тип занятости", this.validator::isSalary),
                FsmStates.ASK_EMPLOYMENT_TYPE, new StepConfig(null, null, null)
        );
        this.userService = userService;
    }

    @Transactional
    public BotResponse start(Long chatId) {
        var user = userService.getUserById(chatId);
        user.setState(FsmStates.ASK_POSITION);

        if (user.getUserRequest() == null) {
            user.setUserRequest(new UserQuery());
        }

        userService.updateUser(user);
        return BotResponse.post(chatId, "Введите должность:");
    }

    public BotResponse processStep(Long chatId, String input) {
        var user = userService.getUserById(chatId);
        var state = user.getState();
        var step = steps.get(state);

        if (step.getValidation() != null && !step.getValidation().test(input)) {
            return BotResponse.error(chatId, "Введены невалидные данные.");
        }


    }
}
