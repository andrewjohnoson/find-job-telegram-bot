package com.zhevlakov.findjobtelegrambot.vacancy.provider;

import com.zhevlakov.findjobtelegrambot.vacancy.VacancyDto;

import java.util.List;

public interface VacancyProvider {
    List<VacancyDto> fetch(Long userId);
}
