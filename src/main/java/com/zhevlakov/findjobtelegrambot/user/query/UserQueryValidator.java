package com.zhevlakov.findjobtelegrambot.user.query;

import com.zhevlakov.findjobtelegrambot.fsm.FsmStates;
import com.zhevlakov.findjobtelegrambot.user.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserQueryValidator {

    private final UserQueryService userQueryService;

    public UserQueryValidator(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    public boolean isPosition(String position) {
        return position.matches("^(\\D*)$");
    }

    public boolean isCity(String city) {
        return city.matches("^[А-Яа-яЁё]+$");
    }

    public boolean isSalary(String s) {
        return s.matches("^[0-9]+(-[0-9]+)?$");
    }

    public boolean canKeepPrevPosition(UserEntity user) {
        var query = userQueryService.getByChatId(user.getChatId());
        return !user.getState().equals(FsmStates.ASK_POSITION) ||
                query.getPosition() != null;
    }
}
