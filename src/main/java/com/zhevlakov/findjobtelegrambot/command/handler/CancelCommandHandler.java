package com.zhevlakov.findjobtelegrambot.command.handler;

import com.pengrad.telegrambot.model.Message;
import com.zhevlakov.findjobtelegrambot.KeyboardGenerator;
import com.zhevlakov.findjobtelegrambot.bot.BotResponse;
import com.zhevlakov.findjobtelegrambot.command.CommandHandler;
import com.zhevlakov.findjobtelegrambot.command.CommandHandlerName;
import com.zhevlakov.findjobtelegrambot.fsm.FsmStates;
import com.zhevlakov.findjobtelegrambot.fsm.QueryFsmWizardService;
import com.zhevlakov.findjobtelegrambot.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CancelCommandHandler implements CommandHandler {
    private final Logger log = LoggerFactory.getLogger(CancelCommandHandler.class);
    private final UserService userService;
    private final KeyboardGenerator keyboardGenerator;
    private final QueryFsmWizardService wizardService;

    public CancelCommandHandler(
            UserService userService,
            KeyboardGenerator keyboardGenerator,
            QueryFsmWizardService wizardService
    ) {
        this.userService = userService;
        this.keyboardGenerator = keyboardGenerator;
        this.wizardService = wizardService;
    }

    @Override
    public BotResponse handle(Message message) {
        var chatId = message.chat().id();
        if (userService.haveUser(chatId) && userService.isUserFree(chatId)) {
            var keyboard = keyboardGenerator.getStartCommandKeyboard();
            log.error("Пользователь не может отменить создание запроса, так как запрос не создаётся.");
            return BotResponse.error(chatId, "Вы не создаёте запрос в данный момент.", keyboard);
        }

        return wizardService.cancel(chatId);
    }

    @Override
    public CommandHandlerName getCommandHandlerName() {
        return CommandHandlerName.CANCEL;
    }
}
