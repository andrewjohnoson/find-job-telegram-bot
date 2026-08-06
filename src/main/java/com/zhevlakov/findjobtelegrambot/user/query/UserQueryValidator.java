package com.zhevlakov.findjobtelegrambot.user.query;

import org.springframework.stereotype.Component;

@Component
public class UserQueryValidator {
    public boolean isPosition(String position) {
        return !position.contains("0123456789");
    }
}
