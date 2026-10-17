package com.zhevlakov.findjobtelegrambot.vacancy.provider.arbeitnow;

import java.util.List;

public record ArbeitnowResponse(
        List<ArbeitnowJob> jobs
) {
}
