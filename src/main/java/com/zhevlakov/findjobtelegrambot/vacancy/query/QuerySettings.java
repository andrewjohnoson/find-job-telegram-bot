package com.zhevlakov.findjobtelegrambot.vacancy.query;

import java.util.Set;

public record QuerySettings(
        String position,
        Set<String> experience,
        String city,
        Set<String> workFormat,
        String salary,
        Set<String> employmentType,
        String additionParams
) {
    public static String buildQuery() {
        return "";
    }
}
