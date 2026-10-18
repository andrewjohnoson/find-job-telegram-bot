package com.zhevlakov.findjobtelegrambot.vacancy.provider.arbeitnow;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ArbeitnowResponse(
        @JsonProperty("data") List<ArbeitnowJob> jobs
) {
}
