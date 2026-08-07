package com.zhevlakov.findjobtelegrambot.fsm;

import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.zhevlakov.findjobtelegrambot.KeyboardGenerator;
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

    public QueryFsmWizardService(
            UserService userService,
            List<FsmStep> fsmStepList,
            UserQueryService queryService
    ) {
        this.steps = fsmStepList.stream()
                .collect(Collectors.toMap(
                        FsmStep::currentState,
                        Function.identity(),
                        (existing, replacement) -> existing,
                        HashMap::new
                ));
        this.userService = userService;
        this.queryService = queryService;
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

    public BotResponse processChoice(Long chatId, String value) {

    }

    private BotResponse buildPost(UserEntity user, FsmStep step) {
        var keyboard = switch (step.inputType()) {
            case USUAL_TEXT -> null;
            case REPLY_CHOICE -> new ReplyKeyboardMarkup("Some text");
            case INLINE_CHOICE -> KeyboardGenerator.buildInlineKeyboard(step);
        };

        return BotResponse.post(user.getChatId(), step.nextResponseMessage(), keyboard);
    }

    public BotResponse processStep(Long chatId, String input) {
        var user = userService.getUserById(chatId);
        var state = user.getState();
        var step = steps.get(state);

        if (step == null) {
            log.error("Неверное состояние={}", state);
            return BotResponse.error(chatId, "Нету такого обработчика.");
        }

        if (step.validator() != null && !step.validator().test(input)) {
            log.error("Введены невалидные данные={} при обработке в состоянии={}", input, state);
            return BotResponse.error(chatId, "Введены невалидные данные.");
        }

        var query = queryService.getByChatId(chatId);

        step.setProperty(query, input);
        user.setState(step.nextState());

        queryService.updateQuery(query);
        userService.updateUser(user);
    }



}
