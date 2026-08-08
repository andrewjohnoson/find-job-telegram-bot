package com.zhevlakov.findjobtelegrambot.fsm;

import com.zhevlakov.findjobtelegrambot.keyboard.KeyboardGenerator;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.user.UserEntity;
import com.zhevlakov.findjobtelegrambot.user.UserService;
import com.zhevlakov.findjobtelegrambot.user.query.UserQuery;
import com.zhevlakov.findjobtelegrambot.user.query.UserQueryService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class QueryFsmWizardService {
    private final Logger log = LoggerFactory.getLogger(QueryFsmWizardService.class);

    private final Map<FsmStates, FsmStep> steps;
    private final UserService userService;
    private final UserQueryService queryService;
    private final KeyboardGenerator keyboardGenerator;

    public QueryFsmWizardService(
            UserService userService,
            List<FsmStep> fsmStepList,
            UserQueryService queryService,
            KeyboardGenerator keyboardGenerator
    ) {
        this.steps = fsmStepList.stream()
                .collect(Collectors.toMap(
                        FsmStep::currentState,
                        Function.identity(),
                        (existing, _) -> existing,
                        HashMap::new
                ));
        this.userService = userService;
        this.queryService = queryService;
        this.keyboardGenerator = keyboardGenerator;
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

    @Transactional
    public BotResponse cancel(Long chatId) {
        var user = userService.getUserById(chatId);
        user.setState(FsmStates.FREE);
        userService.updateUser(user);
        var keyboard = keyboardGenerator.getStartCommandKeyboard();

        return BotResponse.post(chatId, "Создание запроса отменено.", keyboard);
    }

    private FsmStep getCurrentStep(UserEntity user) {
        var userState = user.getState();
        FsmStep currentStep = steps.get(userState);
        if (currentStep == null) {
            log.error("Нет шага с таким состоянием = {}", userState.toString());
            throw new IllegalStateException("Нет такого состояния.");
        }

        return currentStep;
    }

    @Transactional
    public BotResponse processUsualInput(Long chatId, String input) {
        var user = userService.getUserById(chatId);
        var step = getCurrentStep(user);

        if (step.inputType().equals(InputType.INLINE_CHOICE)) {
            log.error("Пользователь вводит текст, когда нужно выбирать вариант ответа.");
            return BotResponse.error(chatId, "Необходимо выбрать вариант ответа.");
        }

        return applyInput(user, step, input);
    }

    @Transactional
    public BotResponse processChoice(Long chatId, String value) {
        var user = userService.getUserById(chatId);
        var step = getCurrentStep(user);

        return applyInput(user, step, value);
    }

    private BotResponse applyInput(UserEntity user, FsmStep step, String input) {
        var chatId = user.getChatId();

        if (step.validator() != null && !step.validator().test(input)) {
            log.error("Введены невалидные данные={} при обработке в состоянии={}", input, user.getState());
            return BotResponse.error(chatId, "Введены невалидные данные.");
        }

        var query = queryService.getByChatId(chatId);

        step.setProperty(query, input);
        user.setState(step.nextState());

        queryService.updateQuery(query);
        userService.updateUser(user);

        var nextState = getCurrentStep(user);

        return buildPost(user, nextState);
    }

    private BotResponse buildPost(UserEntity user, FsmStep step) {
        var keyboard = switch (step.inputType()) {
            case USUAL_TEXT -> keyboardGenerator.getKeepPrevStateKeyboard(user.getState().toString());
            case REPLY_CHOICE -> null;
            case INLINE_CHOICE -> keyboardGenerator.buildInlineKeyboard(step);
        };

        return BotResponse.post(user.getChatId(), step.nextResponseMessage(), keyboard);
    }
}
