package com.zhevlakov.findjobtelegrambot.callback.handler;

import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.callback.CallbackContent;
import com.zhevlakov.findjobtelegrambot.callback.CallbackHandler;
import com.zhevlakov.findjobtelegrambot.fsm.FsmStateCode;
import com.zhevlakov.findjobtelegrambot.fsm.FsmStates;
import com.zhevlakov.findjobtelegrambot.fsm.QueryFsmWizardService;
import com.zhevlakov.findjobtelegrambot.user.UserService;
import com.zhevlakov.findjobtelegrambot.user.query.UserQueryValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NextCallbackHandler implements CallbackHandler {
    private final QueryFsmWizardService wizardService;
    private final Logger log = LoggerFactory.getLogger(NextCallbackHandler.class);
    private final UserService userService;
    private final UserQueryValidator userQueryValidator;

    public NextCallbackHandler(
            QueryFsmWizardService wizardService,
            UserService userService,
            UserQueryValidator userQueryValidator
    ) {
        this.wizardService = wizardService;
        this.userService = userService;
        this.userQueryValidator = userQueryValidator;
    }

    @Override
    public BotResponse handle(CallbackContent callbackContent) {
        var chatId = callbackContent.chatId();

        log.info("{}", callbackContent.data());
        var code = callbackContent.code();
        var stateFromCode = FsmStates.valueOf(code);

        if (!userService.userHaveState(chatId, stateFromCode)) {
            log.warn("Пользователь = {} попытался нажать кнопку для состояния = {}, находясь в ином состоянии", chatId, code);
            return BotResponse.error(chatId, "Кнопка не откликается.");
        }

        var user = userService.getUserById(chatId);

        if (!userQueryValidator.canKeepPrevPosition(user)) {
            log.error("processChoice: В данный момент должность пользователя = {} не задана, поэтому не можем продолжить. chatId={}",
                    user.getUserTag(), user.getChatId());
            return BotResponse.error(user.getChatId(), "В данный момент должность не задана, поэтому нельзя продолжить.");
        }

        return wizardService.processChoice(chatId, null);
    }

    @Override
    public FsmStateCode queryCode() {
        return FsmStateCode.NEXT;
    }
}
