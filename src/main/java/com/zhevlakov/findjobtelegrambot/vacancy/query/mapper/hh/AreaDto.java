package com.zhevlakov.findjobtelegrambot.vacancy.query.mapper.hh;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AreaDto(
        String id,
        String name,
        @JsonProperty("parent_id") String parentId,
        List<AreaDto> areas
) {
}
