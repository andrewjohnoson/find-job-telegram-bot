package com.zhevlakov.findjobtelegrambot.vacancy.query.mapper;

import java.util.Map;

public record PlatformConfig(
        String baseUrl,
        Map<String, String> queryParamNames,
        Map<String, String> experienceMapping,
        Map<String, String> employmentMapping,
        Map<String, String> workFormatMapping,
        String additionParams
) {
}
