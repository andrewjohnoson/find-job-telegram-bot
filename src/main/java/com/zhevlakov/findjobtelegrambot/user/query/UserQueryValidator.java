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
        return city.matches("^(\\D*)$");
    }

    public boolean isExperience(String experience) {
        try {
            var experienceNumber = Integer.parseInt(experience);
            return experienceNumber > 0 && experienceNumber < 60;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isSalary(String s) {
        return true;
    }

    public boolean canKeepPrevPosition(UserEntity user, String input) {
        var query = userQueryService.getByChatId(user.getChatId());
        return !user.getState().equals(FsmStates.ASK_POSITION) &&
                query.getPosition() != null &&
                input != null;
    }
}
