package com.zhevlakov.findjobtelegrambot.vacancy.provider.remotive;

import java.util.List;

public record RemotiveResponse(
        List<RemotiveJob> jobs
) {
}
