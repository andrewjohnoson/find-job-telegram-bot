package com.zhevlakov.findjobtelegrambot.vacancy.query.mapper.hh;

import java.util.List;

public record SuggestResponse(
        List<SuggestItem> items
) {
}
