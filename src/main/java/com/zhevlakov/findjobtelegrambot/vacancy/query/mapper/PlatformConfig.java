package com.zhevlakov.findjobtelegrambot.vacancy.query.mapper;

import com.zhevlakov.findjobtelegrambot.vacancy.query.converter.EmploymentType;
import com.zhevlakov.findjobtelegrambot.vacancy.query.converter.Experience;
import com.zhevlakov.findjobtelegrambot.vacancy.query.converter.WorkFormat;

import java.util.Map;

public record PlatformConfig(
        String baseUrl,
        String apiUrl,
        Map<String, String> queryParamNames,
        Map<Experience, String> experienceMapping,
        Map<EmploymentType, String> employmentMapping,
        Map<WorkFormat, String> workFormatMapping,
        String additionalParams
) {
}
