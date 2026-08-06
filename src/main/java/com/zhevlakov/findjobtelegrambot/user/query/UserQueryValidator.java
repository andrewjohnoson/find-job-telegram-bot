package com.zhevlakov.findjobtelegrambot.user.query;

import org.springframework.stereotype.Component;

@Component
public class UserQueryValidator {

    public boolean isPosition(String position) {
        return !position.contains("0123456789");
    }

    public boolean isCity(String city) {
        return !city.contains("0123456789");
    }

    public boolean isExperience(String experience) {
        try {
            var experienceNumber = Integer.parseInt(experience);
            return experienceNumber > 0 && experienceNumber < 60;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
