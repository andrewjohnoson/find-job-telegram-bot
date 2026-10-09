package com.zhevlakov.findjobtelegrambot.vacancy.provider.remotive;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record RemotiveJob(
    String title,
    String url,
    String salary,
    String description,
    @JsonProperty("company_name") String companyName,
    @JsonProperty("publication_date") LocalDateTime publicationDate
) {
}
